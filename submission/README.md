# Stock Market Candle Data Aggregation

Submission layout for the stock market data aggregation assignment.

## Project structure

```
submission/
├── server/          # Spring Boot REST API (Java 17, Maven)
├── client/          # Backend CLI client (Java 17, Maven)
├── ingestion/       # One-time CSV → Cassandra utility (Part 1.3)
├── data/            # Place stock_data.csv here (not in git)
├── web-client/      # Optional Vue.js frontend
├── schema.cql       # Apache Cassandra keyspace and table definitions
└── README.md
```

## Prerequisites

- Java 17+
- Maven 3.9+
- Apache Cassandra 4.x (local instance on port 9042)
- Node.js 18+ (optional, for `web-client`)

---

## Cassandra (local)

The server connects to Cassandra at `localhost:9042`, keyspace `stock_keyspace`, datacenter `datacenter1`. Schema is **not** created by the application (`schema-action: none`); apply `schema.cql` manually.

### Start Cassandra locally

**Docker (example):**

```bash
docker run -d --name cassandra-stock \
  -p 9042:9042 \
  cassandra:4.1
```

Wait until the node is ready (typically 30–60 seconds on first start).

**Native install:** start the Cassandra service for your OS (e.g. `brew services start cassandra` on macOS with Homebrew).

### Check Cassandra status

```bash
# Docker
docker ps --filter name=cassandra-stock

# Node readiness (Docker)
docker exec cassandra-stock nodetool status
```

### Open cqlsh

```bash
# Local cqlsh
cqlsh localhost 9042

# Or via Docker
docker exec -it cassandra-stock cqlsh
```

### Apply schema

From the `submission` directory (or pass the full path to `schema.cql`):

```bash
cqlsh -f schema.cql
```

Inside `cqlsh` you can also run:

```cql
SOURCE 'schema.cql';
```

### Verify keyspace and table

```cql
DESCRIBE KEYSPACE stock_keyspace;

SELECT * FROM stock_keyspace.stock_candles LIMIT 5;
```

---

## Data ingestion (Part 1.3)

`stock_data.csv` is **not** committed to git (see root `.gitignore`). Place it locally at:

`submission/data/stock_data.csv`

Columns: `symbol`, `datetime`, `open`, `high`, `low`, `close`, `volume` (1-minute candles).

**Assumption:** `datetime` is parsed in `Asia/Kolkata` and stored as `trading_date` + `candle_time` in Cassandra.

### Run ingestion (from repo root)

Prerequisites: Cassandra running, `schema.cql` applied, CSV in place.

```bash
cd /Users/shikharsingh/stock-candles
mvn -f submission/pom.xml -pl ingestion package exec:java \
  -Dexec.args="--file submission/data/stock_data.csv"
```

Verify sample rows:

```bash
docker exec cassandra-stock cqlsh -e \
  "SELECT symbol, trading_date, candle_time, open, close, volume FROM stock_keyspace.stock_candles LIMIT 5;"
```

---

## Spring Boot server

Configured in `server/src/main/resources/application.yml`:

| Setting           | Value            |
|-------------------|------------------|
| Host              | `localhost`      |
| Port              | `9042`           |
| Keyspace          | `stock_keyspace` |
| Local datacenter  | `datacenter1`    |
| Schema action     | `none`           |
| HTTP port         | `8080`           |

### Start the server

Ensure Cassandra is running and `schema.cql` has been applied, then:

```bash
cd server
mvn spring-boot:run
```

On startup, logs include Cassandra host, port, keyspace, and local datacenter.

### API docs

- Swagger UI: http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

### Test

```bash
cd server
mvn test
```

---

## Client

CLI that calls the REST API.

```bash
cd client
mvn package
java -jar target/stock-candles-client-1.0.0-SNAPSHOT.jar AAPL 15m "2024-01-15 09:15:00" "2024-01-15 15:30:00"
```

Optional pagination is supported by adding page and size arguments:

```bash
java -jar target/stock-candles-client-1.0.0-SNAPSHOT.jar AAPL 15m "2024-01-15 09:15:00" "2024-01-15 15:30:00" 0 100
```

Environment variable `STOCK_API_BASE_URL` defaults to `http://localhost:8080`.

---

## Web client (optional)

```bash
cd web-client
npm install
npm run dev
```

The Vue app supports symbol/timeframe/date-range queries and renders a candlestick chart.

---

## Verify Cassandra connection from the server

1. Start Cassandra and apply `schema.cql` (steps above).
2. Start the server: `cd server && mvn spring-boot:run`.
3. Confirm log lines similar to:
   - `Cassandra host: localhost`
   - `Cassandra port: 9042`
   - `Cassandra keyspace: stock_keyspace`
   - `Local datacenter: datacenter1`
4. If Cassandra is down, the application will fail to start when the driver cannot connect.

---

## Server packages

Layered packages under `com.stockcandles`:

| Package     | Responsibility                |
|------------|-------------------------------|
| controller | REST endpoints                |
| service    | Business logic                |
| repository | Cassandra data access         |
| model      | Cassandra entities            |
| dto        | API request/response objects  |
| enums      | Domain enumerations           |
| exception  | Errors and global handler     |
| config     | Cache, OpenAPI, startup logging |
| util       | Mapping and helpers           |

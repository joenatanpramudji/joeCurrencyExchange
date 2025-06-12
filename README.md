Currency Exchange Microservice

A small Spring Boot application that fetches live USD-based exchange rates, stores them in an H2 database, and exposes endpoints to convert any currency pair via USD as an intermediary.

🔧 Prerequisites
- Java 17 (or 11+)
- Maven (or use the included wrapper `./mvnw`)
- Internet access to reach the exchange-rate API

⚙️ Setup & Run

1. Clone the repo
   git clone https://github.com/yourusername/currency-service.git
   cd currency-service

2. Configure your API key
   Edit `src/main/resources/application.properties` (or supply environment variables) to point at your provider:

   # CurrencyLayer “live” endpoint
   exchange.api.base-url=https://api.currencylayer.com/live
   exchange.api.key=YOUR_ACCESS_KEY_HERE

   Tip: If you use exchangerate.host (no key), set
   exchange.api.base-url=https://api.exchangerate.host/live
   exchange.api.key=

3. Build & start the application
   ./mvnw clean package
   java -jar target/currency-service-0.0.1-SNAPSHOT.jar

   The app will start on port 8080 by default.

4. Seed the database
   Fetch live rates into H2 before converting:
   curl http://localhost:8080/admin/fetch-all
   Response:
   Fetched all live rates

🛠️ Available Endpoints

1. Convert currencies
   GET /api/convert

   Query parameters
   - from – source currency code (e.g. IDR)
   - to – target currency code (e.g. USD)
   - amount – numeric amount to convert

   Example
   curl -G http://localhost:8080/api/convert \
        --data-urlencode "from=IDR" \
        --data-urlencode "to=USD" \
        --data-urlencode "amount=10000"

   Response
   {
     "base":      "IDR",
     "target":    "USD",
     "amount":    10000,
     "rate":      0.0000615,
     "converted": 0.615
   }

2. List supported currencies
   GET /api/currencies

   Returns a JSON array of all currency codes you can convert (including USD).

   curl http://localhost:8080/api/currencies

🗄️ H2 Database Console (optional)

1. Open your browser → http://localhost:8080/h2-console
2. JDBC URL:
   jdbc:h2:mem:currencydb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
3. User: sa
4. Password: (leave blank)

⚙️ Docker (optional)

Build and run in Docker:
docker build -t currency-service .
docker run -p 8080:8080 currency-service

📄 Testing

- Unit & integration tests are included.
- Run all tests with:
  ./mvnw test

🙋‍♂️ Troubleshooting

- No rates found: make sure you’ve called /admin/fetch-all or scheduled your fetch at startup.
- Invalid currency code: only codes present in /api/currencies work.

Feel free to file issues or pull requests if you find bugs or want enhancements!

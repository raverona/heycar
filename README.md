# heycar

1- Why Postgre?  
Postgre seemed a good choice because car listings tend to have a fixed schema, it can handle queries by multiple columns if the table size is not really huge, which seems to be the case

2- CSV file format  
I'm assuming the csv file format is fixed and every row always has the following values `code,make/model,power-in-ps,year,color,price` although in the order defined by the header

3- Indexes on columns make, model, year and color 
The indexes created on these columns are (model), (model, year), (model, color) and (model, year, color) because I thought these would be the most common criteria used

4- Search criteria available  
The search criteria available are only values equals make, model, year or color, for the sake of simplicity but since I implemented using Spring's Specification it could be expandable to other operands like greater or less than

5- Does the `/vehicle_listings/` endpoint receives an dealer_id?  
Since the dealer_id is part of the composite identifier of each listing (along with 'code'), I'm assuming that the endpoint is actually `/vehicle_listings/{dealer_id}` and receives the dealer_id much like the `/upload_csv/{dealer_id}` endpoint

6- How to run?  
Go to the root of the project and:  
1- start the local environment by executing `docker-compose up -d`  
2- start the project by executing `./gradlew bootRun`
package project.model;


import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;

import java.io.IOException;
import java.util.Iterator;

public class Auction {
    private String auctionCreatorName;
    private int price;
    private String bestBuyerName;
    private int auctionCode;
    private boolean isActivated;
    private String cardName;
    private int timeLeftAsSeconds;
    //    private static HashMap<String, Integer>
//    private static String addressOfStorage = "Resourses\\Server\\";

    public Auction(String auctionCreatorName, int initialPrice, String cardName) {
        this.auctionCreatorName = auctionCreatorName;
        this.price = initialPrice;
        this.bestBuyerName = "null";
        this.auctionCode = calculateAuctionCode();
        this.isActivated = true;
        this.cardName = cardName;
        this.timeLeftAsSeconds = 60;

        writeInformationInDataBase();
//        try {
//            FileWriter fileWriter = new FileWriter(
//                addressOfStorage + "Auctions\\" + getAuctionCode() + ".json");
//            fileWriter.write(toGsonFormat(this));
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(0);
//        }

        startTimeDecreasing(this);


    }

    private void writeInformationInDataBase() {
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            String sql = "INSERT INTO COMPANY (auctionCode,cardName,auctionCreatorName,bestBuyerName,price,isActivated,timeLeftAsSeconds) " +
//                "VALUES ('" + auctionCode + "', '" + cardName + "', '" + auctionCreatorName + "', '" +
//                bestBuyerName + "', '" + price + "', '" + isActivated + "', '" + timeLeftAsSeconds + "');";
//            stmt.executeUpdate(sql);
//
//
//            stmt.close();
//            c.commit();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Records created successfully");

//        auctionCreatorName
//            price = initialPric
//        bestBuyerName = "nu
//        auctionCode = calcu
//        isActivated = true;
//        cardName = cardName
//        timeLeftAsSeconds =
        //TODO: MongoDB
        System.out.println("\n\n\n\n\n\n\n\n\ntaaakkdkjdj\n\n\n\n\ng");
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");
        Document document = new Document("auctionCode", String.valueOf(auctionCode))
            .append("cardName", cardName)
            .append("auctionCreatorName", auctionCreatorName)
            .append("bestBuyerName", bestBuyerName)
            .append("price", String.valueOf(price))
            .append("isActivated", String.valueOf(isActivated))
            .append("timeLeftAsSeconds", String.valueOf(timeLeftAsSeconds));
        //Inserting document into the collection
        collection.insertOne(document);
        System.out.println("Document inserted successfully");

    }

    public static String getCardNameByAuctionCode(int parseInt) {
        //TODO: JSON and file
//        FileReader fileReader = null;
//        try {
//            fileReader = new FileReader(addressOfStorage + "Auctions\\" + parseInt + ".json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Scanner myReader = new Scanner(fileReader);
//        String informationOfUser = myReader.nextLine();
//        JsonParser parser = new JsonParser();
//        JsonElement rootNode = parser.parse(informationOfUser);
//        myReader.close();
//        try {
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (rootNode.isJsonObject()) {
//            JsonObject details = rootNode.getAsJsonObject();
//            return details.get("cardName").getAsString();
//        }
//        return null;
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//        String answer = "";
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
//
//
//            while ( rs.next() ) {
//                String auctionCode = rs.getString("auctionCode");
//                if (Integer.parseInt(auctionCode) == parseInt) {
//                    answer = rs.getString("cardName");
//                }
//            }
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//        return answer;
        //TODO: MongoDB
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");

        // Getting the iterable object
//        FindIterable<Document> iterDoc = collection.find();
//        int i = 1;
//        // Getting the iterator
//        Iterator it = iterDoc.iterator();
//        while (it.hasNext()) {
//            Document document = (Document) it.next();
//            if (document.get("auctionCode").equals(String.valueOf(parseInt))) {
//                return (String) document.get("cardName");
//            }
//            i++;
//        }
        String answer = collection.distinct("cardName", Filters.eq("auctionCode",String.valueOf(parseInt)), String.class).first();
        return answer;
    }

    public static String getBuyerNameByAuctionCode(String auctionCode) {
        //TODO: JSON
//        FileReader fileReader = null;
//        try {
//            fileReader = new FileReader(addressOfStorage + "Auctions\\" + auctionCode + ".json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Scanner myReader = new Scanner(fileReader);
//        String informationOfUser = myReader.nextLine();
//        JsonParser parser = new JsonParser();
//        JsonElement rootNode = parser.parse(informationOfUser);
//        myReader.close();
//        try {
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (rootNode.isJsonObject()) {
//            JsonObject details = rootNode.getAsJsonObject();
//            return details.get("bestBuyerName").getAsString();
//        }
//        return null;
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//        String answer = "";
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
//
//
//            while ( rs.next() ) {
//                String auctionCode1 = rs.getString("auctionCode");
//                if (auctionCode1.equals(auctionCode)) {
//                    answer = rs.getString("bestBuyerName");
//                }
//            }
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//        return answer;
        //TODO: MongoDB
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");

        // Getting the iterable object
//        FindIterable<Document> iterDoc = collection.find();
//        int i = 1;
//        // Getting the iterator
//        Iterator it = iterDoc.iterator();
//        while (it.hasNext()) {
//            Document document = (Document) it.next();
//            if (document.get("auctionCode").equals(auctionCode)) {
//                return (String) document.get("bestBuyerName");
//            }
//            i++;
//        }
//        return null;
        String answer = collection.distinct("bestBuyerName", Filters.eq("auctionCode", auctionCode), String.class).first();
        return answer;
    }

    public static int getNumberOfAllAuctons() {
        return calculateAuctionCode() - 1;
    }

    public static int getPriceByCode(String auctionCode) {
        //TODO: JSON
//        FileReader fileReader = null;
//        try {
//            fileReader = new FileReader(addressOfStorage + "Auctions\\" + auctionCode + ".json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Scanner myReader = new Scanner(fileReader);
//        String informationOfUser = myReader.nextLine();
//        JsonParser parser = new JsonParser();
//        JsonElement rootNode = parser.parse(informationOfUser);
//        myReader.close();
//        try {
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (rootNode.isJsonObject()) {
//            JsonObject details = rootNode.getAsJsonObject();
//            return details.get("price").getAsInt();
//        }
//        return 0;
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//        int answer = 0;
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
//
//
//            while ( rs.next() ) {
//                String auctionCode1 = rs.getString("auctionCode");
//                if (auctionCode1.equals(auctionCode)) {
//                    answer = Integer.parseInt(rs.getString("price"));
//                }
//            }
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//        return answer;
        //TODO: MongoDB
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");

        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();
        int i = 1;
        String answer = "";
        // Getting the iterator
//        Document document = collection.find(Filters.eq("auctionCode",auctionCode)).projection(include("bestBuyerName")).first();
//        answer = document.getString("price");
        answer = collection.distinct("price", Filters.eq("auctionCode",auctionCode), String.class).first();
        System.out.println("PRICE:" + answer);
//        Iterator it = iterDoc.iterator();
//        while (it.hasNext()) {
//            Document document = (Document) it.next();
//            if (document.get("auctionCode").equals(auctionCode)) {
//                answer = (String) document.get("price");
//            }
//            i++;
//        }
        return Integer.parseInt(answer);
    }

    public static void changeAuctionBuyerAndPrice(String auctionCode, String name, int suggestedPrice) {
        //TODO: JSON
//        FileReader fileReader = null;
//        try {
//            fileReader = new FileReader(addressOfStorage + "Auctions\\" + auctionCode + ".json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Scanner myReader = new Scanner(fileReader);
//        String informationOfUser = myReader.nextLine();
//        JsonParser parser = new JsonParser();
//        JsonElement rootNode = parser.parse(informationOfUser);
//        myReader.close();
//        try {
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String cardName = "";
//        String creatorName = "";
//        String isActivated = "";
//        String time = "";
//
//        if (rootNode.isJsonObject()) {
//            JsonObject details = rootNode.getAsJsonObject();
//            cardName = details.get("cardName").getAsString();
//            creatorName = details.get("auctionCreatorName").getAsString();
//            isActivated = details.get("isActivated").getAsString();
//            time = details.get("timeLeftAsSeconds").getAsString();
//        }
//
//
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("auctionCode", auctionCode);
//        jsonObject.addProperty("cardName", cardName);
//        jsonObject.addProperty("auctionCreatorName", creatorName);
//        jsonObject.addProperty("bestBuyerName", name);
//        jsonObject.addProperty("price", suggestedPrice);
//        jsonObject.addProperty("isActivated", isActivated);
//        jsonObject.addProperty("timeLeftAsSeconds", time);
//
//
//        try {
//            FileWriter fileWriter = new FileWriter(
//                addressOfStorage + "Auctions\\" + auctionCode + ".json");
//            fileWriter.write(jsonObject.toString());
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(0);
//        }
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            String sql = "UPDATE COMPANY set bestBuyerName = '" + name + "' where auctionCode='" + auctionCode + "';";
//            stmt.executeUpdate(sql);
//            String sql2 = "UPDATE COMPANY set price = '" + suggestedPrice + "' where auctionCode='" + auctionCode + "';";
//            stmt.executeUpdate(sql2);
//            c.commit();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
        //TODO: MongoDB
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
            "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");
        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection myCollection selected successfully");
        collection.updateOne(Filters.eq("auctionCode", auctionCode), Updates.set("bestBuyerName", name));
        collection.updateOne(Filters.eq("auctionCode", auctionCode), Updates.set("price", String.valueOf(suggestedPrice)));
        System.out.println("Document update successfully...");


    }

    public static String getIsActivated(String auctionCode) {
        //TODO: JSON
//        FileReader fileReader = null;
//        try {
//            fileReader = new FileReader(addressOfStorage + "Auctions\\" + auctionCode + ".json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        Scanner myReader = new Scanner(fileReader);
//        String informationOfUser = myReader.nextLine();
//        JsonParser parser = new JsonParser();
//        JsonElement rootNode = parser.parse(informationOfUser);
//        myReader.close();
//        try {
//            fileReader.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        if (rootNode.isJsonObject()) {
//            JsonObject details = rootNode.getAsJsonObject();
//            return details.get("isActivated").getAsString();
//        }
//        return null;
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//        String answer = "";
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
//
//
//            while ( rs.next() ) {
//                String auctionCode1 = rs.getString("auctionCode");
//                if (auctionCode1.equals(auctionCode)) {
//                    answer = rs.getString("isActivated");
//                }
//            }
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//        return answer;
        //TODO: MongoDB
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");

        // Getting the iterable object
//        FindIterable<Document> iterDoc = collection.find();
//        int i = 1;
//        // Getting the iterator
//        Iterator it = iterDoc.iterator();
//        while (it.hasNext()) {
//            Document document = (Document) it.next();
//            if (document.get("auctionCode").equals(auctionCode)) {
//                return (String) document.get("isActivated");
//            }
//            i++;
//        }
//        return null;
        String answer = collection.distinct("isActivated", Filters.eq("auctionCode", auctionCode), String.class).first();
        return answer;

    }

    private void startTimeDecreasing(Auction auction) {
        new Thread(() -> {
            for (int i = 0; i < 59; i++) {
                try {
                    Thread.sleep(1000);
                    decreaseTimeByOne(auction, 0);
                    auction.timeLeftAsSeconds = auction.getTimeLeftAsSeconds() - 1;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            decreaseTimeByOne(auction, 1);
        }).start();
    }

    private static void decreaseTimeByOne(Auction auction, int i) {
        //TODO: JSON and file
//        try {
//            FileWriter fileWriter = new FileWriter(
//                addressOfStorage + "Auctions\\" + auction.getAuctionCode() + ".json");
//            fileWriter.write(toGsonFormatForSecond(auction));
//            fileWriter.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//            System.exit(0);
//        }

        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            String sql = "UPDATE COMPANY set timeLeftAsSeconds = '" + (auction.getTimeLeftAsSeconds() - 1) + "' where auctionCode='" + auction.getAuctionCode() + "';";
//            stmt.executeUpdate(sql);
//            if (i == 1) {
//                String sql2 = "UPDATE COMPANY set isActivated = 'false' where auctionCode='" + auction.getAuctionCode() + "';";
//                stmt.executeUpdate(sql2);
//            }
//            c.commit();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
        //TODO: MongoDB
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb",
            "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");
        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection myCollection selected successfully");
        String auctionCode = String.valueOf(auction.getAuctionCode());
        int time = auction.getTimeLeftAsSeconds();
        String newTime = String.valueOf(time - 1);
        collection.updateOne(Filters.eq("auctionCode", auctionCode), Updates.set("timeLeftAsSeconds", newTime));
        if (i == 1) {
            collection.updateOne(Filters.eq("auctionCode", auctionCode), Updates.set("isActivated", "false"));
        }
        System.out.println("Document update successfully...");
    }

//    private static String toGsonFormatForSecond(Auction auction) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("auctionCode", auction.getAuctionCode());
//        jsonObject.addProperty("cardName", auction.getCardName());
//        jsonObject.addProperty("auctionCreatorName", auction.getAuctionCreatorName());
//        jsonObject.addProperty("bestBuyerName", auction.getBestBuyerName());
//        jsonObject.addProperty("price", auction.getPrice());
//        jsonObject.addProperty("isActivated", auction.getIsActivated());
//        jsonObject.addProperty("timeLeftAsSeconds", auction.getTimeLeftAsSeconds() - 1);
//        return jsonObject.toString();
//    }

    private static int calculateAuctionCode() {
        //TODO: JSON
//        List<String> results = new ArrayList<String>();
//        File[] files = new File("Resourses\\Server\\Auctions\\").listFiles();
//
//        for (File file : files) {
//            if (file.isFile()) {
//                results.add(file.getName());
//            }
//        }
//
//        int answer = 1;
//        for (String result : results) {
//            answer++;
//        }
//        return answer;
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//        int count = 1;
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
//            while ( rs.next() ) {
//                count++;
//            }
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//        return count;
        //TODO: MongoDB
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");
        // Getting the iterable object
        FindIterable<Document> iterDoc = collection.find();
        int i = 1;
        // Getting the iterator
        Iterator it = iterDoc.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
            System.out.println("101010");
            i++;
        }
//        System.out.println(i + "1\n1\n1\n1\n1\n1\n1n1\n");
        return i;
    }

//    private String toGsonFormat(Auction auction) {
//        JsonObject jsonObject = new JsonObject();
//        jsonObject.addProperty("auctionCode", auction.getAuctionCode());
//        jsonObject.addProperty("cardName", auction.getCardName());
//        jsonObject.addProperty("auctionCreatorName", auction.getAuctionCreatorName());
//        jsonObject.addProperty("bestBuyerName", auction.getBestBuyerName());
//        jsonObject.addProperty("price", auction.getPrice());
//        jsonObject.addProperty("isActivated", auction.getIsActivated());
//        jsonObject.addProperty("timeLeftAsSeconds", auction.getTimeLeftAsSeconds());
//        return jsonObject.toString();
//    }

    private String getCardName() {
        return this.cardName;
    }

    public String getAuctionCreatorName() {
        return this.auctionCreatorName;
    }

    public String getBestBuyerName() {
        return this.bestBuyerName;
    }

    public int getPrice() {
        return this.price;
    }

    public int getTimeLeftAsSeconds() {
        return this.timeLeftAsSeconds;
    }

    public void setNewSuggestion(String username, int newPrice) {
        this.bestBuyerName = username;
        this.price = newPrice;
        //Change database
    }

    public int getAuctionCode() {
        return this.auctionCode;
    }

    private boolean getIsActivated() {
        return this.isActivated;
    }

    public static String getAllAuctions() throws IOException {
        //TODO: JSON
//        String answer = "";
//
//        List<String> allFileNames = new ArrayList<String>();
//
//        File[] files = new File("Resourses\\Server\\Auctions\\").listFiles();
//        for (File file : files) {
//            if (file.isFile()) {
//                allFileNames.add(file.getName());
//            }
//        }
//
//        for (int i = 0; i < allFileNames.size(); i++) {
//
//            FileReader fileReader = new FileReader(addressOfStorage + "Auctions\\" + allFileNames.get(i));
//
//            Scanner myReader = new Scanner(fileReader);
//            String informationOfUser = myReader.nextLine();
//            JsonParser parser = new JsonParser();
//            JsonElement rootNode = parser.parse(informationOfUser);
//            myReader.close();
//            fileReader.close();
//
//            if (rootNode.isJsonObject()) {
//                JsonObject details = rootNode.getAsJsonObject();
//                answer += details.get("auctionCode").getAsString();
//                answer += ",";
//                answer += details.get("cardName").getAsString();
//                answer += ",";
//                answer += details.get("auctionCreatorName").getAsString();
//                answer += ",";
//                answer += details.get("bestBuyerName").getAsString();
//                answer += ",";
//                answer += details.get("price").getAsString();
//                answer += ",";
//                answer += details.get("isActivated").getAsString();
//                answer += ",";
//                answer += details.get("timeLeftAsSeconds").getAsString();
//                answer += ",";
//            }
//        }
//        return answer;
        //TODO: SQL
//        Connection c = null;
//        Statement stmt = null;
//        String answer = "";
//        try {
//            Class.forName("org.sqlite.JDBC");
//            c = DriverManager.getConnection("jdbc:sqlite:Resourses\\Server\\Auctions\\test.db");
//            c.setAutoCommit(false);
//            System.out.println("Opened database successfully");
//
//            stmt = c.createStatement();
//            ResultSet rs = stmt.executeQuery( "SELECT * FROM COMPANY;" );
//
//
//            while ( rs.next() ) {
//                answer += rs.getString("auctionCode");
//                answer += ",";
//                answer += rs.getString("cardName");
//                answer += ",";
//                answer += rs.getString("auctionCreatorName");
//                answer += ",";
//                answer += rs.getString("bestBuyerName");
//                answer += ",";
//                answer += rs.getString("price");
//                answer += ",";
//                answer += rs.getString("isActivated");
//                answer += ",";
//                answer += rs.getString("timeLeftAsSeconds");
//                answer += ",";
//            }
//            rs.close();
//            stmt.close();
//            c.close();
//        } catch ( Exception e ) {
//            System.err.println( e.getClass().getName() + ": " + e.getMessage() );
//            System.exit(0);
//        }
//        System.out.println("Operation done successfully");
//        return answer;
        //TODO: MongoDB

        // Creating a Mongo client
        MongoClient mongo = new MongoClient( "localhost" , 27017 );

        // Creating Credentials
        MongoCredential credential;
        credential = MongoCredential.createCredential("sampleUser", "myDb", "password".toCharArray());
        System.out.println("Connected to the database successfully");

        // Accessing the database
        MongoDatabase database = mongo.getDatabase("myDb");

        // Retrieving a collection
        MongoCollection<Document> collection = database.getCollection("sampleCollection");
        System.out.println("Collection sampleCollection selected successfully");

        // Getting the iterable object
//        FindIterable<Document> iterDoc = collection.find();
//        int i = 1;
//        // Getting the iterator
//        Iterator it = iterDoc.iterator();
//        while (it.hasNext()) {
//            System.out.println(it.next());
//            i++;
//        }

        String answer = "";
        int currentNumberOfAuctions = getNumberOfAllAuctons();
        for (int i = 1; i <= currentNumberOfAuctions; i++) {
            String auctionCodeString = String.valueOf(i);
            answer += collection.distinct("auctionCode", Filters.eq("auctionCode", auctionCodeString), String.class).first();
            answer += ",";
            answer += collection.distinct("cardName", Filters.eq("auctionCode", auctionCodeString), String.class).first();
            answer += ",";
            answer += collection.distinct("auctionCreatorName", Filters.eq("auctionCode", auctionCodeString), String.class).first();
            answer += ",";
            answer += collection.distinct("bestBuyerName", Filters.eq("auctionCode", auctionCodeString), String.class).first();
            answer += ",";
            answer += collection.distinct("price", Filters.eq("auctionCode", auctionCodeString), String.class).first();
            answer += ",";
            answer += collection.distinct("isActivated", Filters.eq("auctionCode", auctionCodeString), String.class).first();
            answer += ",";
            answer += collection.distinct("timeLeftAsSeconds", Filters.eq("auctionCode", auctionCodeString), String.class).first();
            answer += ",";

        }
        return answer;
    }
}

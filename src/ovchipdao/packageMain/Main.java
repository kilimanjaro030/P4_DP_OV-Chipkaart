package ovchipdao.packageMain;

import ovchipdao.packageDomain.OVChipkaart;
import ovchipdao.packageDomain.Product;
import ovchipdao.packagePersistence.*;
import ovchipdao.packageDomain.Adres;
import ovchipdao.packageDomain.Reiziger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.SQLOutput;
import java.util.List;

public class Main {
    private static Connection connection;

    public static void main(String[] args) throws SQLException {
        ReizigerDAO rdao = new ReizigerDAOPsql(getConnection(connection));
        testReizigerDAO(rdao);
        closeConnection(getConnection(connection));
        AdresDAO adao = new AdresDAOPsql(getConnection(connection));
        testAdresDAO(adao,rdao);
        OVChipkaartDAO odao = new OVChipkaartDAOPsql(getConnection(connection));
        testOVChipkaartDAO(odao,rdao);
        ProductDAO pdao = new ProductDAOPsql(getConnection(connection));
        testProductDAO(pdao,odao);
    }

    private static Connection getConnection(Connection connection) throws SQLException{
        connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "postgres", "Stabilo12");
        return connection;
    }

    /**
     * P2. Reiziger DAO: persistentie van een klasse
     *
     * Deze methode sluit onze database connectie met onze Postgres database
     *
     * @throws SQLException
     */
    private static void closeConnection(Connection connection) throws SQLException {
        connection.close();
        System.out.println("Close Connection");
    }
    private static void testReizigerDAO(ReizigerDAO rdao) throws SQLException {
        System.out.println("\n---------- Test ReizigerDAO -------------");
        System.out.println();

        // Haal alle reizigers op uit de database
        System.out.println("\n---------- Haal alle reizigers op uit de database -------------");
        System.out.println();
        List<Reiziger> reizigers = rdao.findAll();
        System.out.println("[Test] ReizigerDAO.findAll() geeft de volgende reizigers:");
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        System.out.println();

        // Maak een nieuwe reiziger aan en persisteer deze in de database
        System.out.println("\n---------- Maak een nieuwe reiziger aan en persisteer deze in de database -------------");
        System.out.println();
        String gbdatum = "1981-03-14";
        Reiziger sietske = new Reiziger(77, "S", "", "Boers", java.sql.Date.valueOf(gbdatum));
        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.save(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // verwijder reiziger
        System.out.println("\n---------- verwijder reiziger -------------");
        System.out.println();

        System.out.print("[Test] Eerst " + reizigers.size() + " reizigers, na ReizigerDAO.save() ");
        rdao.delete(sietske);
        reizigers = rdao.findAll();
        System.out.println(reizigers.size() + " reizigers\n");

        // update reiziger
        System.out.println("\n---------- update reiziger -------------");
        System.out.println();
        rdao.update(new Reiziger(77, "M", "", "Boers", java.sql.Date.valueOf(gbdatum)));
        System.out.println();
        for (Reiziger r : reizigers) {
            System.out.println(r);
        }
        // findbyid reiziger
        System.out.println("\n---------- findbyid reiziger -------------");
        System.out.println();
        System.out.println(rdao.findById(6));

        // findByGbdatum gbdatum
        System.out.println("\n---------- findByGbdatum reiziger -------------");
        System.out.println();
        reizigers = rdao.findByGbdatum("10-11-1999");

        for(Reiziger reiziger : reizigers){
            System.out.println(reiziger);
        }
    }

    private static void testAdresDAO(AdresDAO adao, ReizigerDAO rdao) throws SQLException {
        adao.setRdao(rdao);
        // alle Adressen op uit de database
        System.out.println("\n---------- alle Adressen op uit de database -------------");
        System.out.println();
        List<Adres> adressen = adao.findAll();
        System.out.println("[Test] AdresDAO.findAll() geeft de volgende reizigers:");
        for (Adres a : adressen) {
            System.out.println(a);
        }
        System.out.println();

        // nieuwe adres aanmaken
        System.out.println("\n---------- nieuwe adres aanmaken -------------");
        System.out.println();

        String mehmetBD = "1999-11-10";
        Reiziger mehmet = new Reiziger(79, "M.A", "", "Bayram", java.sql.Date.valueOf(mehmetBD));
        rdao.save(mehmet);
        System.out.println("Eerst " + adressen.size() + " adressen, na adao.save() ");
        Adres padualaan = new Adres(12,"3535SB","31","Padualaan","Utrecht");

        padualaan.setReiziger(mehmet);
        adao.save(padualaan);
        adressen = adao.findAll();
        System.out.println(adressen.size() + " adressen.\n");

        System.out.println("\n---------- update adres -------------");
        System.out.println();
        System.out.println("Padualaan voor update:"  );
        adressen = adao.findAll();
        for(Adres adres : adressen){
            System.out.println(adres);
        }
        padualaan = new Adres(12, "3535SB", "69", "Padualaan", "Utrecht");
        adao.update(padualaan);
        System.out.println();
        System.out.println("Padualaan na update:");

        adressen = adao.findAll();

        for(Adres adres: adressen){
            System.out.println(adres);
        }

        System.out.println();

        System.out.println("\n---------- FindByReiziger -------------");
        System.out.println();
        String hansBD = "1945-08-01";
        Reiziger hans = new Reiziger(5,"H.","","Klaasen",java.sql.Date.valueOf(hansBD));
        Adres add = adao.findByReiziger(hans);
        System.out.println(add);

        System.out.println("\n---------- delete adres -------------");
        adressen = adao.findAll();
        System.out.println("Aantal adressen voor delete = " + adressen.size());
        adao.delete(padualaan);
        adressen = adao.findAll();
        System.out.println("Aantal adressen na delete = " + adressen.size());

        rdao.delete(mehmet);

        System.out.println();
    }

    private static void testOVChipkaartDAO(OVChipkaartDAO odao, ReizigerDAO rdao){
        odao.setRdao(rdao);

        System.out.println("\n---------- alle ovchipkaarten op uit de database -------------");
        System.out.println();
        List<OVChipkaart> ovchipkaarten = odao.findAll();

        for(OVChipkaart ovChipkaart : ovchipkaarten){
            System.out.println(ovChipkaart);
        }
        System.out.println("\n---------- nieuwe ovchipkaart aanmaken -------------");
        System.out.println();

        Reiziger mehmet = new Reiziger(79, "M.A", "", "Bayram", java.sql.Date.valueOf("1999-11-10"));
        rdao.save(mehmet);

        ovchipkaarten = odao.findAll();

        System.out.println("Eerst " + ovchipkaarten.size() + " ovchipkaarten, na odao.save() ");

        OVChipkaart ovchipkaart = new OVChipkaart(31691,java.sql.Date.valueOf("2031-12-31"),1,25);

        ovchipkaart.setReiziger(mehmet);
        odao.save(ovchipkaart);

        ovchipkaarten = odao.findAll();
        System.out.println(ovchipkaarten.size() + " ovchipkaarten.\n");

        System.out.println();


        System.out.println("\n---------- update adres -------------");
        ovchipkaarten = odao.findAll();
        System.out.println("OVChipkaart voor de update:");
        for(OVChipkaart ovChipkaart: ovchipkaarten){
            System.out.println(ovChipkaart);
        }

        ovchipkaart = new OVChipkaart(31691,java.sql.Date.valueOf("2031-12-31"),2,25);
        odao.update(ovchipkaart);
        System.out.println("OVChipkaart na update");
        ovchipkaarten = odao.findAll();
        for(OVChipkaart ovChipkaart: ovchipkaarten){
            System.out.println(ovChipkaart);
        }


        System.out.println();

        System.out.println("\n---------- FindByReiziger -------------");
        Reiziger reiziger = rdao.findById(79);
        ovchipkaarten = odao.findByReiziger(reiziger);

        for(OVChipkaart ovChipkaart : ovchipkaarten){
            System.out.println(ovChipkaart);
        }
        System.out.println("\n---------- delete ovchipkaart -------------");

        ovchipkaarten = odao.findAll();
        System.out.println("Eerst " + ovchipkaarten.size() + " ovchipkaarten");
        odao.delete(ovchipkaart);

        ovchipkaarten = odao.findAll();

        System.out.println("Na delete " + ovchipkaarten.size() + "ovchipkaarten");

        rdao.delete(mehmet);

    }

    private static void testProductDAO(ProductDAO pdao, OVChipkaartDAO odao) {
        pdao.setOdao(odao);

        System.out.println("\n---------- ALle producten ophalen van de database met findAll -------------");
        System.out.println();
        List<Product> producten = pdao.findAll();

        for(Product product : producten){
            System.out.println(product);
        }

        System.out.println();

        System.out.println("\n---------- Product toevoegen in de database -------------");
        System.out.println();

        producten = pdao.findAll();
        System.out.print("Aantal producten in de database voor het creeren van een product: " + producten.size());
        Product product = new Product(10, "StudentenOV","OV product voor studenten",00.00);

        OVChipkaart ovChipkaart = odao.findByid(79625);
        OVChipkaart ovChipkaart1 = odao.findByid(90537);

        product.AddOVChipkaart(ovChipkaart);
        product.AddOVChipkaart(ovChipkaart1);

        pdao.save(product);

        producten = pdao.findAll();
        System.out.println(producten.size() + " Producten\n");

        System.out.println();

        System.out.println("\n---------- Product aanpassen in de database -------------");
        System.out.println();

        System.out.println("Product voordat de aanpassing zich plaatsvond:");
        Product productnr10 = pdao.findById(10);
        System.out.println(productnr10);

        System.out.println();

        Product newProductnr10 = new Product(10,"StudentenOV","Nieuwe wettelijke regeling StudentenOV is vanaf nu per jaar 10 euro",10.00);
        pdao.update(newProductnr10);

        System.out.println("Product na de aanpassingen: ");
        newProductnr10 = pdao.findById(10);
        System.out.println(newProductnr10);

        System.out.println();

        System.out.println("\n---------- Product verwijderen van de database -------------");
        System.out.println();

        producten = pdao.findAll();

        System.out.println("Aantal producten in de database voor het verwijderen van een product: " + producten.size());

        product.DeleteOVChipkaart(ovChipkaart);
        product.DeleteOVChipkaart(ovChipkaart);
        pdao.delete(product);


        producten = pdao.findAll();
        System.out.println(producten.size() + " OVChipkaarten\n");

        System.out.println();

        System.out.println("\n---------- selecteer producten die bij een ovchipkaart horen. -------------");
        System.out.println();
        System.out.println("functie findbyovchipkaart geeft de volgende product: ");

        OVChipkaart oneOVChipkaart = odao.findByid(90537);
        System.out.println(oneOVChipkaart);
        List<Product> products = pdao.findByOVChipkaart(oneOVChipkaart);

        for (Product p : products){
            System.out.println(p);
        }
        System.out.println();

        System.out.println("\n---------- selecteer ovchipkaarten met een product -------------");
        System.out.println();

        System.out.println("functie findbyproduct geeft de volgende product: ");

        Product oneProduct = pdao.findById(3);
        List<OVChipkaart> ovChipkaarten = odao.findByProduct(oneProduct);

        for(OVChipkaart ov : ovChipkaarten){
            System.out.println(ov);
        }
        System.out.println();
    }
}
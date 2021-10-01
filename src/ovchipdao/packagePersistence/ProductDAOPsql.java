package ovchipdao.packagePersistence;

import ovchipdao.packageDomain.OVChipkaart;
import ovchipdao.packageDomain.Product;
import ovchipdao.packageDomain.Reiziger;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductDAOPsql implements ProductDAO {
    private Connection conn;
    private OVChipkaartDAO odao;

    public ProductDAOPsql(Connection connection) {
        this.conn = connection;
    }

    public static Connection getConnection() throws SQLException {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/ovchip", "postgres", "Stabilo12");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public void setOdao(OVChipkaartDAO odao) {
        this.odao = odao;
    }

    public boolean save(Product product) {
        boolean result = false;
        try (Connection conn = getConnection()) {
            PreparedStatement pt = conn.prepareStatement("INSERT INTO product (product_nummer , naam, beschrijving , prijs) VALUES (?,?,?,?)");
            pt.setInt(1, product.getProduct_nummer());
            pt.setString(2, product.getNaam());
            pt.setString(3, product.getBeschrijving());
            pt.setDouble(4, product.getPrijs());
            pt.executeUpdate();

            for(OVChipkaart ovChipkaart : product.getOvchipkaarten()){
                String relation = "INSERT INTO ov_chipkaart_product VALUES (?,?,?,?)";
                PreparedStatement ptR= conn.prepareStatement(relation);
                ptR.setInt(1, ovChipkaart.getKaart_nummer());
                ptR.setInt(2, product.getProduct_nummer());
                ptR.setString(3, null);
                ptR.setDate(4, null);
                ptR.executeUpdate();
            }

            result = true;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return result;
    }

    public boolean update(Product product){
        boolean result = false;

        String query = "UPDATE product SET naam = ?, beschrijving = ?, prijs = ?" + " WHERE product_nummer= ?";
        String queryRelation = "UPDATE ov_chipkaart_product SET last_update = ?" + "WHERE product_nummer = ?";
        try {
            PreparedStatement pt = conn.prepareStatement(query);

            pt.setString(1, product.getNaam());
            pt.setString(2, product.getBeschrijving());
            pt.setDouble(3, product.getPrijs());
            pt.setInt(4, product.getProduct_nummer());
            result = pt.executeUpdate() > 0;

            PreparedStatement ptRelation = conn.prepareStatement(queryRelation);
            ptRelation.setDate(1, Date.valueOf(LocalDate.now()));
            ptRelation.setInt(2, product.getProduct_nummer());
            result = ptRelation.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;

    }

    public boolean delete(Product product){
        boolean result = false;

        String queryRelation = "DELETE FROM ov_chipkaart_product WHERE product_nummer = ?";
        String query = "DELETE FROM product WHERE product_nummer = ?";

        try{
            PreparedStatement ptRelation = conn.prepareStatement(queryRelation);
            ptRelation.setInt(1, product.getProduct_nummer());
            result = ptRelation.executeUpdate() > 0;


            PreparedStatement pt = conn.prepareStatement(query);
            pt.setInt(1,product.getProduct_nummer());
            result = pt.executeUpdate() > 0;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return result;
    }

    public List<Product> findByOVChipkaart(OVChipkaart ovchipkaart) {
        List<Product> producten = ovchipkaart.getProducten();
        try {
            String query =  "SELECT * FROM product p JOIN ov_chipkaart_product ovp ON p.product_nummer = ovp.product_nummer WHERE ovp.kaart_nummer = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            preparedStatement.setInt(1, ovchipkaart.getKaart_nummer());
            ResultSet rs = preparedStatement.executeQuery();

            while (rs.next()) {
                int productnummer = rs.getInt("product_nummer");
                String productnaam = rs.getString("naam");
                String productbeschrijving = rs.getString("beschrijving");
                double prijs = rs.getInt("prijs");

                producten.add(new Product(productnummer, productnaam, productbeschrijving, prijs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return producten;
    }

    public List<Product> findAll() {
        List<Product> results = new ArrayList<>();
        try (Connection conn = getConnection()) {
            String query = "SELECT * FROM product";
            Statement pt = conn.createStatement();
            ResultSet result = pt.executeQuery(query);
            while (result.next()) {
                int product_nummer = result.getInt(1);
                String naam = result.getString(2);
                String beschrijving = result.getString(3);
                double prijs = result.getDouble(4);

                Product product = new Product(product_nummer, naam, beschrijving, prijs);
                results.add(product);
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return results;
    }
    public Product findById(int id){
        Product result = null;
        String query = "SELECT product_nummer, naam, beschrijving, prijs FROM product WHERE product_nummer = ?";

        try{
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                int product_nummer = rs.getInt("product_nummer");
                String naam = rs.getString("naam");
                String beschrijving = rs.getString("beschrijving");
                double prijs = rs.getDouble("prijs");

                result = new Product(product_nummer,naam,beschrijving,prijs);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
}

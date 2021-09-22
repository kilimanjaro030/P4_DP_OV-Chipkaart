package ovchipdao.packagePersistence;

import ovchipdao.packageDomain.Reiziger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OVChipkaartDAOPsql implements OVChipkaartDAO {
    private Connection conn;
    private ReizigerDAO rdao;
    public OVChipkaartDAOPsql(Connection connection) {
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

    public void setRdao(ReizigerDAO rdao){
        this.rdao = rdao;
    }

    public boolean save(OVChipkaart ovchipkaart) {
        boolean result = false;
        try(Connection conn = getConnection()){
            PreparedStatement pt = conn.prepareStatement("INSERT INTO ov_chipkaart (kaart_nummer , geldig_tot, klasse , saldo, reiziger_id) VALUES (?,?,?,?,?)");
            pt.setInt(1,ovchipkaart.getKaart_nummer());
            pt.setDate(2, ovchipkaart.getGeldig_tot());
            pt.setInt(3, ovchipkaart.getKlasse());
            pt.setInt(4, ovchipkaart.getSaldo());
            pt.setInt(5, ovchipkaart.getReiziger().getId());
            pt.executeUpdate();
            result = true;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return result;
    }
    public boolean update(OVChipkaart ovchipkaart) {
        boolean result = false;
        try(Connection conn = getConnection()){
            PreparedStatement pt = conn.prepareStatement("UPDATE ov_chipkaart SET geldig_tot = ?, klasse = ?, saldo = ?" + " WHERE kaart_nummer = '"+ ovchipkaart.getKaart_nummer() + "'");

            pt.setDate(1, ovchipkaart.getGeldig_tot());
            pt.setInt(2, ovchipkaart.getKlasse());
            pt.setInt(3, ovchipkaart.getSaldo());

            pt.executeUpdate();
            result = true;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return result;
    }
    public boolean delete(OVChipkaart ovchipkaart) {
        boolean result = false;
        try (Connection conn = getConnection()) {
            PreparedStatement pt = conn.prepareStatement("DELETE from ov_chipkaart WHERE kaart_nummer = '" + ovchipkaart.getKaart_nummer() + "'");
            pt.executeUpdate();
            result = true;
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return result;
    }

    public List<OVChipkaart> findByReiziger(Reiziger reiziger) {
            List<OVChipkaart> lijst_ovchipkaarten = reiziger.getOvChipkaarten();
            String query = "SELECT kaart_nummer, geldig_tot, klasse, saldo FROM ov_chipkaart WHERE reiziger_id = '"+ reiziger.getId() + "'";
            try{
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(query);
                while (rs.next()) {
                    int kaart_nummer = rs.getInt("kaart_nummer");
                    Date geldig_tot = rs.getDate("geldig_tot");
                    int klasse = rs.getInt("klasse");
                    int saldo = rs.getInt("saldo");
                    lijst_ovchipkaarten.add(new OVChipkaart(kaart_nummer,geldig_tot,klasse,saldo));
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return lijst_ovchipkaarten;
    }
    public List<OVChipkaart> findAll() {
        List<OVChipkaart> results = new ArrayList<>();
        try(Connection conn = getConnection()){
            String query = "SELECT * FROM ov_chipkaart";
            Statement pt = conn.createStatement();
            ResultSet result = pt.executeQuery(query);
            while (result.next()) {
                int kaart_nummer = result.getInt(1);
                Date geldig_tot = result.getDate(2);
                int klasse = result.getInt(3);
                int saldo = result.getInt(4);
                int reiziger_id = result.getInt(5);

                OVChipkaart ovChipkaart =  new OVChipkaart (kaart_nummer,geldig_tot,klasse,saldo);
                ovChipkaart.setReiziger(rdao.findById(reiziger_id));

                results.add(ovChipkaart);
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return results;
    }
}

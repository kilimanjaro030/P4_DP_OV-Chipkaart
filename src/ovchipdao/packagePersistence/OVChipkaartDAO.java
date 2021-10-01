package ovchipdao.packagePersistence;

import ovchipdao.packageDomain.OVChipkaart;
import ovchipdao.packageDomain.Product;
import ovchipdao.packageDomain.Reiziger;

import java.util.List;

public interface OVChipkaartDAO {
    void setRdao(ReizigerDAO reiziger);
    public boolean save(OVChipkaart ovchipkaart);
    public boolean update(OVChipkaart ovchipkaart);
    public boolean delete(OVChipkaart ovchipkaart);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findAll();
    public OVChipkaart findByid(int id);
    public List<OVChipkaart> findByProduct(Product product);
}
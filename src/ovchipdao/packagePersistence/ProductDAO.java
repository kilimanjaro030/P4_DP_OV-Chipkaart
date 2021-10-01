package ovchipdao.packagePersistence;

import ovchipdao.packageDomain.OVChipkaart;
import ovchipdao.packageDomain.Product;
import ovchipdao.packageDomain.Reiziger;

import java.util.List;

public interface ProductDAO {
    void setOdao(OVChipkaartDAO odao);
    public boolean save(Product product);
    public boolean update(Product product);
    public boolean delete(Product product);
    public List<Product> findByOVChipkaart(OVChipkaart ovchipkaart);
    public List<Product> findAll();
    public Product findById(int id);
}

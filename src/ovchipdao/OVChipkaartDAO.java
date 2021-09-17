package ovchipdao;

import java.util.List;

public interface OVChipkaartDAO {
    void setRdao(ReizigerDAO reiziger);
    public boolean save(OVChipkaart ovchipkaart);
    public boolean update(OVChipkaart ovchipkaart);
    public boolean delete(OVChipkaart ovchipkaart);
    public List<OVChipkaart> findByReiziger(Reiziger reiziger);
    public List<OVChipkaart> findAll();
}

package at.campus02.dbp;

import at.campus02.dbp.model.Schrank;
import at.campus02.dbp.model.Standort;

import java.util.List;

public interface SchrankDAO {

    Schrank findById(Long id);


    Schrank save(Schrank schrank);      // CREATE

    Schrank update(Schrank schrank);    // UPDATE

    void delete(Schrank schrank);       // DELETE

    void deleteById(Long id);

}
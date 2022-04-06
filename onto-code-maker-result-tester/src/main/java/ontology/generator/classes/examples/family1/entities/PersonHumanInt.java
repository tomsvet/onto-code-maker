package ontology.generator.classes.examples.family1.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;

/**
*   This is interface representing equivalence of classes
*
*   Generated by OntoCodeMaker
**/
public interface PersonHumanInt extends  OntoEntity {


    void setHasAge(int hasAge);
    int getHasAge();
    void setHasDog(Dog hasDog);
    Dog getHasDog();
    Dog getHasDogEq();
    void addHasCat(Cat hasCat);
    List<Cat> getHasCat();

}

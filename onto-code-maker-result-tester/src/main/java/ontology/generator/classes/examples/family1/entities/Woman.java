package ontology.generator.classes.examples.family1.entities;

import org.eclipse.rdf4j.model.*;
import java.util.List;
import java.util.ArrayList;
import ontology.generator.classes.examples.family1.Vocabulary;

/**
*  This is the class representing the Woman class from ontology
*
*   Generated by OntoCodeMaker
**/
public class Woman extends  Human {

    // IRI Constant of Class
    public static IRI CLASS_IRI = Vocabulary.WOMAN_CLASS_IRI;

    /**
    * Property http://example.com/hasHusband
    **/
    private List<Men> hasHusband = new ArrayList<>();

    /**
    * Property http://example.com/men
    **/
    private Men men;


    public Woman(IRI iri){
            super(iri);
    }


    public IRI getClassIRI() {
        return CLASS_IRI;
    }

    public void addHasHusband(Men hasHusband){
        this.hasHusband.add(hasHusband);
    }
    public List<Men> getHasHusband(){
        return hasHusband;
    }

    public void setMen(Men men){
        this.men = men;
    }
    public Men getMen(){
        return men;
    }



}


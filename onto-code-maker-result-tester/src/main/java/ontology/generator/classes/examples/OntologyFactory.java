package ontology.generator.classes.examples;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.util.Values;

import java.util.Collection;

public class OntologyFactory {
    private Model ontology;
    private HumanSerialization humanSerialization;

    public OntologyFactory(Model ontology){
        this.ontology = ontology;
        this.humanSerialization = new HumanSerialization();
    }

    public Model getOntology(){
        return ontology;
    }

    public Human createHuman(String name){
        return new Human(Values.iri(name));
    }

    public void addToModel(OntoEntity entity){
        if (entity instanceof Human){
            humanSerialization.addToModel(ontology, (Human) entity);
        }
    }

    public Human getHumanFromModel(String name){
        return humanSerialization.getInstanceFromModel(ontology,Values.iri(name));
    }

    public Collection<Human> getAllHumanInstancesFromModel(){
        return humanSerialization.getAllInstancesFromModel(ontology);
    }

    public void removeHumanFromModel(String name){
        humanSerialization.removeInstanceFromModel(ontology,Values.iri(name));
    }


}

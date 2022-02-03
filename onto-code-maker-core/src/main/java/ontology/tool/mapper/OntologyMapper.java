package ontology.tool.mapper;

import ontology.tool.generator.ClassRepresentation;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Resource;
import org.eclipse.rdf4j.model.vocabulary.OWL;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.model.vocabulary.RDFS;

import java.util.*;


public class OntologyMapper {
    private Model model;
    List<IRI> classPredIRIs = new ArrayList<>(Arrays.asList(RDFS.CLASS,OWL.CLASS));


    public OntologyMapper(Model newModel){
        model = newModel;
    }


    public void mapping(Model model){




    }

    public List<ClassRepresentation> mapClasses(){
        List<ClassRepresentation> allClassRep = new ArrayList<>();
        Set<Resource> classes = getClasses();
        for(Resource classResource:classes){
            if(classResource.isIRI()){
                IRI classIRI = (IRI) classResource;
                ClassRepresentation classRep = new ClassRepresentation(classIRI.getNamespace(),classIRI.getLocalName());
                allClassRep.add(classRep);
            }
        }
        return allClassRep;
    }

    public Set<Resource> getClasses(){
        Set<Resource> allClasses = new HashSet<>();

        for(IRI classPredIRI:classPredIRIs){
            Set<Resource> classes = model.filter(null, RDF.TYPE, classPredIRI).subjects();
            allClasses.addAll(classes);
        }
        return allClasses;
    }


    public void getOWLOntologies(){
        if (model != null){
            Set<Resource> owlOntologies = model.filter(null, RDF.TYPE, OWL.ONTOLOGY).subjects();
        }
    }

}

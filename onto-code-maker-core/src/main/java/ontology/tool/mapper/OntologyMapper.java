package ontology.tool.mapper;

import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.EntityRepresentation;
import ontology.tool.generator.representations.PropertyRepresentation;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.vocabulary.*;

import java.util.*;


public class OntologyMapper {
    private Model model;
    List<ClassRepresentation> mappedClasses;
    List<IRI> CLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.CLASS,OWL.CLASS));
    List<IRI> COMMENT_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.COMMENT, DCTERMS.DESCRIPTION,
            SKOS.DEFINITION, DC.DESCRIPTION));
    List<IRI> LABEL_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.LABEL, DCTERMS.TITLE, DC.TITLE, SKOS.PREF_LABEL, SKOS.ALT_LABEL,SKOS.HIDDEN_LABEL));
    List<IRI> CREATOR_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(DC.CREATOR,DCTERMS.CREATOR));
    List<IRI> SUBCLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.SUBCLASSOF));
    List<IRI> EQUIVALENT_CLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(OWL.EQUIVALENTCLASS));
    //List<IRI> SUBCLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.DOMAIN));


    public OntologyMapper(Model newModel){
        model = newModel;
    }

    public List<ClassRepresentation> getMappedClasses(){
        return mappedClasses;
    }

    public void mapping(){

        mapClasses();

        for(ClassRepresentation classRep:mappedClasses){
            mapClassHierarchy(classRep);
            mapComments(classRep);
            mapLabels(classRep);
            mapCreator(classRep);
            mapProperties(classRep);
        }
    }

    public void mapClasses(){
        List<ClassRepresentation> allClassRep = new ArrayList<>();
        Set<Resource> classes = getClasses();
        for(Resource classResource:classes){
            if(classResource.isIRI()){
                IRI classIRI = (IRI) classResource;
                ClassRepresentation classRep = new ClassRepresentation(classIRI.getNamespace(),classIRI.getLocalName());
                allClassRep.add(classRep);
            }
        }
        mappedClasses = allClassRep;
    }

    public Set<Resource> getClasses(){
        Set<Resource> allClasses = new HashSet<>();

        for(IRI classPredIRI:CLASS_PREDICATE_IRIS){
            Set<Resource> classes = model.filter(null, RDF.TYPE, classPredIRI).subjects();
            allClasses.addAll(classes);
        }
        return allClasses;
    }

    public void mapClassHierarchy(ClassRepresentation classRep){
        Set<IRI> superClasses = getAllIRIObjects(SUBCLASS_PREDICATE_IRIS,classRep.getValueIRI());

        for(IRI superClass: superClasses){
            ClassRepresentation superClassRep = getMappedClass(superClass);
            if(superClassRep != null){
                classRep.addSuperClasses(superClassRep);
                superClassRep.addSubClasses(classRep);
            }
        }
    }

    public void mapEquivalentClasses(ClassRepresentation classRep){
        Set<IRI> equivalentClasses = getAllIRIObjects(EQUIVALENT_CLASS_PREDICATE_IRIS,classRep.getValueIRI());
        for(IRI eqClassIRI: equivalentClasses){
            ClassRepresentation eqClassRep = getMappedClass(eqClassIRI);
            if(eqClassRep != null){
                classRep.addEquivalentClasses(eqClassRep);
            }
        }
    }

    public ClassRepresentation getMappedClass(IRI classIRI){
        Optional<ClassRepresentation> res = mappedClasses.stream().filter(classRep -> classRep.getValueIRI().equals(classIRI)).findFirst();
        return res.isPresent()? res.get(): null;
    }

    public boolean isMappedClass(Value classValue){
        Optional<ClassRepresentation> res = mappedClasses.stream().filter(classRep -> classRep.getValueIRI().equals(classValue)).findFirst();
        return res.isEmpty() ? false:true;
    }

    public void mapComments(ClassRepresentation classRep){
        Set<Literal> allComments = getAllLiteralObjects(COMMENT_PREDICATE_IRIS,classRep.getValueIRI());
        for(Literal comment : allComments){
                classRep.addCommentProperty(comment.stringValue());
        }
    }

    public void mapLabels(ClassRepresentation classRep){
        Set<Literal> allLabels = getAllLiteralObjects(LABEL_PREDICATE_IRIS,classRep.getValueIRI());
        for(Literal comment : allLabels){
            classRep.addLabelProperty(comment.stringValue());
        }
    }

    public void mapCreator(ClassRepresentation classRep){
        Literal creator = getFirstLiteralObject(CREATOR_PREDICATE_IRIS,classRep.getValueIRI());
        if ( creator!= null ){
                classRep.setCreator(creator.stringValue());
        }
    }


    public void mapProperties(ClassRepresentation classRep){
        Set<IRI> properties = getAllIRISubjects(RDFS.DOMAIN,classRep.getValueIRI());
        for(IRI propertyIRI : properties){
            IRI typeIRI = getFirstIRIObject(RDF.TYPE, propertyIRI);
            if(typeIRI.equals(OWL.DATATYPEPROPERTY)){
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setPropertyType(PropertyRepresentation.PROPERTY_TYPE.DATATYPE);
                property.setType("String");
                IRI range = getFirstIRIObject(RDFS.RANGE, propertyIRI);

                classRep.addProperties(property);

            }else if(typeIRI.equals(OWL.FUNCTIONALPROPERTY)) {
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setPropertyType(PropertyRepresentation.PROPERTY_TYPE.FUNCTIONAL);
            }
        }
    }


    public Literal getFirstLiteralObject(List<IRI> predicates, IRI subject){
        for(IRI predicate:predicates){
            Literal literal = getFirstLiteralObject(predicate,subject);
            if(literal != null){
                return literal;
            }
        }
        return null;
    }

    public Literal getFirstLiteralObject(IRI predicate, IRI subject){
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object : objects){
            if(object.isLiteral()){
                return (Literal) object;
            }
        }
        return null;
    }

    public IRI getFirstIRIObject(List<IRI> predicates, IRI subject){
        for(IRI predicate:predicates){
            IRI iri = getFirstIRIObject(predicate,subject);
            if(iri != null){
                return iri;
            }
        }
        return null;
    }

    public IRI getFirstIRIObject(IRI predicate, IRI subject){
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object : objects){
            if(object.isIRI()){
                return (IRI) object;
            }
        }
        return null;
    }

    public Set<Literal> getAllLiteralObjects(List<IRI> predicates, IRI subject){
        Set<Literal> allObjectLiterals = new HashSet<>();
        for(IRI predicate:predicates){
            Set<Literal> objects = getAllLiteralObjects(predicate,subject);
            allObjectLiterals.addAll(objects);
        }
        return allObjectLiterals;
    }

    public Set<IRI> getAllIRIObjects(List<IRI> predicates, IRI subject){
        Set<IRI> allObjectLiterals = new HashSet<>();

        for(IRI predicate:predicates){
            Set<IRI> objects = getAllIRIObjects(predicate,subject);
            allObjectLiterals.addAll(objects);

        }
        return allObjectLiterals;
    }

    public Set<Literal> getAllLiteralObjects(IRI predicate, IRI subject){
        Set<Literal> allObjectsLiterals = new HashSet<>();
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object:objects){
            if(object.isLiteral()){
                allObjectsLiterals.add((Literal) object);
            }
        }
        return allObjectsLiterals;
    }

    public Set<IRI> getAllIRIObjects(IRI predicate, IRI subject){
        Set<IRI> allObjectsIRIs = new HashSet<>();
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object:objects){
            if(object.isIRI()){
                allObjectsIRIs.add((IRI) object);
            }
        }
        return allObjectsIRIs;
    }

    public Set<Value> getAllObjects(IRI predicate, IRI subject){
        return model.filter(subject, predicate, null).objects();
    }


    public Set<IRI> getAllIRISubjects(List<IRI> predicate, IRI object){
        Set<IRI> allSubjectsIRI = new HashSet<>();

        for(IRI predIRI:predicate){
            Set<IRI> subjects = getAllIRISubjects(predIRI,object);
            allSubjectsIRI.addAll(subjects);
        }
        return allSubjectsIRI;
    }

    public Set<IRI> getAllIRISubjects(IRI predicate, IRI object){
        Set<IRI> allSubjectsIRIs = new HashSet<>();
        Set<Resource> subjects = getAllSubjects(predicate,object);
        for(Resource subject:subjects){
            if(subject.isIRI()){
                allSubjectsIRIs.add((IRI) subject);
            }
        }
        return allSubjectsIRIs;
    }


    public Set<Resource> getAllSubjects(IRI predicate, IRI object){
        return model.filter(null, predicate, object).subjects();
    }



    public EntityRepresentation getOWLOntology(){
        if (model != null){
            Set<Resource> owlOntologies = model.filter(null, RDF.TYPE, OWL.ONTOLOGY).subjects();
            Resource ontologyResource = owlOntologies.iterator().next();
            if(ontologyResource.isIRI()){
                IRI ontologyIRI = (IRI) ontologyResource;
                EntityRepresentation onto = new EntityRepresentation(ontologyIRI.getNamespace(),ontologyIRI.getLocalName());
                return onto;
            }
        }
        return null;
    }

}

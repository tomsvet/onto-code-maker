package ontology.tool.mapper;

import ontology.tool.generator.OntologyGenerator;
import ontology.tool.generator.language_generators.JavaGenerator;
import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.EntityRepresentation;
import ontology.tool.generator.representations.OntologyRepresentation;
import ontology.tool.generator.representations.PropertyRepresentation;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.*;

import java.util.*;


public class OntologyMapper {
    private Model model;
    List<ClassRepresentation> mappedClasses;
    List<IRI> PRIORVERSION_IRIS = new ArrayList<>(Arrays.asList(OWL.PRIORVERSION));
    List<IRI> IMPORTS_IRIS = new ArrayList<>(Arrays.asList(OWL.IMPORTS));
    List<IRI> CLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.CLASS,OWL.CLASS));
    List<IRI> COMMENT_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.COMMENT, DCTERMS.DESCRIPTION,
            SKOS.DEFINITION, DC.DESCRIPTION));
    List<IRI> LABEL_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.LABEL, DCTERMS.TITLE, DC.TITLE, SKOS.PREF_LABEL, SKOS.ALT_LABEL,SKOS.HIDDEN_LABEL));
    List<IRI> CREATOR_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(DC.CREATOR,DCTERMS.CREATOR));
    List<IRI> SUBCLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.SUBCLASSOF));
    List<IRI> EQUIVALENT_CLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(OWL.EQUIVALENTCLASS));

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
            mapEquivalentClasses(classRep);
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
            }else{
                //todo class does not exist what to do ??
            }
        }
    }

    public void mapEquivalentClasses(ClassRepresentation classRep){
        Set<IRI> equivalentClasses = getAllIRIObjects(EQUIVALENT_CLASS_PREDICATE_IRIS,classRep.getValueIRI());
        for(IRI eqClassIRI: equivalentClasses){
            ClassRepresentation eqClassRep = getMappedClass(eqClassIRI);
            if(eqClassRep != null){
                if(!classRep.getEquivalentClasses().contains(eqClassRep) ){
                    for(ClassRepresentation oldEqClass:classRep.getEquivalentClasses()){
                        if(!oldEqClass.getEquivalentClasses().contains(eqClassRep) ){
                            oldEqClass.addEquivalentClasses(eqClassRep);
                        }
                    }
                    classRep.addEquivalentClasses(eqClassRep);
                }

                if(!eqClassRep.getEquivalentClasses().contains(classRep) ){
                    for(ClassRepresentation oldEqClass:eqClassRep.getEquivalentClasses()) {
                        if(!oldEqClass.getEquivalentClasses().contains(classRep) ){
                            oldEqClass.addEquivalentClasses(classRep);
                        }
                        if(!classRep.getEquivalentClasses().contains(oldEqClass) ){
                            classRep.addEquivalentClasses(oldEqClass);
                        }
                    }
                    eqClassRep.addEquivalentClasses(classRep);
                }

            }else{
                //todo class does not exist what to do ??
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
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.DATATYPE);
                IRI range = getFirstIRIObject(RDFS.RANGE, propertyIRI);
                property.setRangeIRI(range);
                property.setIsFunctional(true);
                classRep.addProperties(property);

                mapEquivalentProperties(classRep,property);

            }else if(typeIRI.equals(OWL.OBJECTPROPERTY)) {
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.OBJECT);
                IRI range = getFirstIRIObject(RDFS.RANGE, propertyIRI);
                if (range == null) {

                }
                property.setRangeIRI(range);
                ClassRepresentation rangeClass = getMappedClass(range);
                if (rangeClass == null) {
                    //todo class is not there
                }
                property.setRangeClass(rangeClass);
                classRep.addProperties(property);

                mapInverseProperties(classRep,property);

                mapFunctionalProperties(property);

                mapEquivalentProperties(classRep,property);

                mapInverseFunctionalProperties(classRep,property);
            }
        }
    }

    public void mapFunctionalProperties(PropertyRepresentation property){
        boolean isFunctionalProperty = existStatementWithIRI(property.getValueIRI(),RDF.TYPE,OWL.FUNCTIONALPROPERTY);
        if(isFunctionalProperty) {
            property.setIsFunctional(true);
        }
    }

    public void mapInverseFunctionalProperties(ClassRepresentation classRep,PropertyRepresentation property){
        boolean isInverseFunctionalProperty = existStatementWithIRI(property.getValueIRI(),RDF.TYPE,OWL.INVERSEFUNCTIONALPROPERTY);
        if(isInverseFunctionalProperty) {
            PropertyRepresentation inverseProperty = createInverseProperty(classRep, property, Values.iri(classRep.getNamespace() + property.getRangeClass().getName().toLowerCase()));
            inverseProperty.setIsFunctional(true);
        }
    }


    public void mapInverseProperties(ClassRepresentation classRep, PropertyRepresentation property){
        ClassRepresentation rangeClass = property.getRangeClass();
        Set<IRI> inverseIRIProperties = getAllIRISubjects(OWL.INVERSEOF, property.getValueIRI());
        for(IRI inversePropertyIRI :inverseIRIProperties){
            PropertyRepresentation inverseProperty = createInverseProperty(classRep, property, inversePropertyIRI);
            /*PropertyRepresentation inverseProperty = new PropertyRepresentation(inversePropertyIRI.getNamespace(), inversePropertyIRI.getLocalName());
            inverseProperty.setRangeIRI(classRep.getValueIRI());
            inverseProperty.setRangeClass(classRep);
            inverseProperty.setClassName(rangeClass.getName());

            mapFunctionalProperties(inverseProperty);

            rangeClass.addProperties(inverseProperty);*/
            //check OWL.INVERSEFUNCTIONALPROPERTY

            //todo check if can be possible inverse inversed property
            mapInverseProperties(rangeClass,inverseProperty);
        }
    }


    public PropertyRepresentation createInverseProperty(ClassRepresentation classRep,PropertyRepresentation property, IRI inversePropertyIRI ){
        PropertyRepresentation inverseProperty = new PropertyRepresentation(inversePropertyIRI.getNamespace(), inversePropertyIRI.getLocalName());
        inverseProperty.setRangeIRI(classRep.getValueIRI());
        inverseProperty.setRangeClass(classRep);
        inverseProperty.setClassName(property.getRangeClass().getName());
        inverseProperty.setType(property.getType());
        mapFunctionalProperties(inverseProperty);
        property.getRangeClass().addProperties(inverseProperty);
        return inverseProperty;
    }



    public void mapEquivalentProperties(ClassRepresentation classRep, PropertyRepresentation property){
        Set<IRI> equivalentProperties = getAllIRISubjects(OWL.EQUIVALENTPROPERTY, property.getValueIRI());
        for(IRI equivalentProperty:equivalentProperties){
            PropertyRepresentation equivalentPropertyRep = new PropertyRepresentation(equivalentProperty.getNamespace(), equivalentProperty.getLocalName());
            equivalentPropertyRep.setRangeIRI(property.getRangeIRI());
            equivalentPropertyRep.setRangeClass(property.getRangeClass());
            equivalentPropertyRep.setType(property.getType());
            equivalentPropertyRep.setValue(property.getValue());
            equivalentPropertyRep.setClassName(property.getClassName());
            //todo need to check if functional property is set in equivalent classes
            equivalentPropertyRep.setIsFunctional(property.isFunctional());
            //mapFunctionalProperties(equivalentPropertyRep);

            classRep.addProperties(equivalentPropertyRep);
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

    public IRI getFirstIRISubject(List<IRI> predicates, IRI object){
        for(IRI predicate:predicates){
            IRI iri = getFirstIRIObject(predicate,object);
            if(iri != null){
                return iri;
            }
        }
        return null;
    }


    public IRI getFirstIRISubject(IRI predicate, IRI object){
        Set<Resource> subjects = getAllSubjects(predicate,object);
        for(Resource subject : subjects){
            if(subject.isIRI()){
                return (IRI) subject;
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

    public boolean existStatementWithIRI(IRI subject,IRI predicate,IRI object){
        Model resultModel =  model.filter(subject, predicate, object);
        return resultModel.size() >0 ? true:false;
    }

    public void mapImports(OntologyRepresentation ontology){
        Set<IRI> imports = getAllIRIObjects(IMPORTS_IRIS,ontology.getValueIRI());
        for(IRI importVal : imports){
            ontology.addImports(importVal.stringValue());
        }
    }

    public void mapPriorVersion(OntologyRepresentation ontology){
        IRI priorVersion = getFirstIRIObject(PRIORVERSION_IRIS,ontology.getValueIRI());
        if ( priorVersion!= null ){
            ontology.setPriorVersion(priorVersion.stringValue());
        }
    }

    public void mapOntologyInformations(OntologyRepresentation ontology){
        mapImports(ontology);
        mapPriorVersion(ontology);
    }

    public OntologyRepresentation getOWLOntology(){
        if (model != null){
            Set<Resource> owlOntologies = model.filter(null, RDF.TYPE, OWL.ONTOLOGY).subjects();
            Resource ontologyResource = owlOntologies.iterator().next();
            if(ontologyResource.isIRI()){
                IRI ontologyIRI = (IRI) ontologyResource;
                OntologyRepresentation onto = new OntologyRepresentation(ontologyIRI.getNamespace(),ontologyIRI.getLocalName());
                return onto;
            }
        }
        return null;
    }

}

package ontology.tool.mapper;

import ontology.tool.generator.representations.ClassRepresentation;
import ontology.tool.generator.representations.OntologyRepresentation;
import ontology.tool.generator.representations.PropertyRepresentation;
import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.*;

import java.util.*;


public class OntologyMapper {
    private ModelManager modelManager;

    List<ClassRepresentation> mappedClasses;
    List<IRI> PRIORVERSION_IRIS = new ArrayList<>(Collections.singletonList(OWL.PRIORVERSION));
    List<IRI> IMPORTS_IRIS = new ArrayList<>(Collections.singletonList(OWL.IMPORTS));
    List<IRI> CLASS_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.CLASS,OWL.CLASS));
    List<IRI> COMMENT_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.COMMENT, DCTERMS.DESCRIPTION,
            SKOS.DEFINITION, DC.DESCRIPTION));
    List<IRI> LABEL_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(RDFS.LABEL, DCTERMS.TITLE, DC.TITLE, SKOS.PREF_LABEL, SKOS.ALT_LABEL,SKOS.HIDDEN_LABEL));
    List<IRI> CREATOR_PREDICATE_IRIS = new ArrayList<>(Arrays.asList(DC.CREATOR,DCTERMS.CREATOR));
    List<IRI> SUBCLASS_PREDICATE_IRIS = new ArrayList<>(Collections.singletonList(RDFS.SUBCLASSOF));
    List<IRI> EQUIVALENT_CLASS_PREDICATE_IRIS = new ArrayList<>(Collections.singletonList(OWL.EQUIVALENTCLASS));

    public OntologyMapper(Model model){
        this.modelManager = new ModelManager(model);
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
            Set<Resource> classes = modelManager.getAllSubjects(RDF.TYPE,classPredIRI);
            allClasses.addAll(classes);
        }
        return allClasses;
    }

    public void mapClassHierarchy(ClassRepresentation classRep){
        Set<IRI> superClasses = modelManager.getAllIRIObjects(SUBCLASS_PREDICATE_IRIS,classRep.getValueIRI());

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
        Set<IRI> equivalentClasses = modelManager.getAllIRIObjects(EQUIVALENT_CLASS_PREDICATE_IRIS,classRep.getValueIRI());
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
        return res.orElse(null);
    }

    public boolean isMappedClass(Value classValue){
        Optional<ClassRepresentation> res = mappedClasses.stream().filter(classRep -> classRep.getValueIRI().equals(classValue)).findFirst();
        return res.isPresent();
    }

    public void mapComments(ClassRepresentation classRep){
        Set<Literal> allComments = modelManager.getAllLiteralObjects(COMMENT_PREDICATE_IRIS,classRep.getValueIRI());
        for(Literal comment : allComments){
                classRep.addCommentProperty(comment.stringValue());
        }
    }

    public void mapLabels(ClassRepresentation classRep){
        Set<Literal> allLabels = modelManager.getAllLiteralObjects(LABEL_PREDICATE_IRIS,classRep.getValueIRI());
        for(Literal comment : allLabels){
            classRep.addLabelProperty(comment.stringValue());
        }
    }

    public void mapCreator(ClassRepresentation classRep){
        Literal creator = modelManager.getFirstLiteralObject(CREATOR_PREDICATE_IRIS,classRep.getValueIRI());
        if ( creator!= null ){
                classRep.setCreator(creator.stringValue());
        }
    }


    public void mapProperties(ClassRepresentation classRep){
        Set<IRI> properties = modelManager.getAllIRISubjects(RDFS.DOMAIN,classRep.getValueIRI());
        for(IRI propertyIRI : properties){
            IRI typeIRI = modelManager.getFirstIRIObject(RDF.TYPE, propertyIRI);
            if(typeIRI.equals(OWL.DATATYPEPROPERTY)){
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.DATATYPE);
                IRI range = modelManager.getFirstIRIObject(RDFS.RANGE, propertyIRI);
                property.setRangeIRI(range);
                property.setIsFunctional(true);
                classRep.addProperties(property);

                mapEquivalentProperties(classRep,property);

            }else if(typeIRI.equals(OWL.OBJECTPROPERTY)) {
                PropertyRepresentation property = new PropertyRepresentation(propertyIRI.getNamespace(), propertyIRI.getLocalName());
                property.setClassName(classRep.getName());
                property.setType(PropertyRepresentation.PROPERTY_TYPE.OBJECT);
                IRI range = modelManager.getFirstIRIObject(RDFS.RANGE, propertyIRI);
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
        boolean isFunctionalProperty = modelManager.existStatementWithIRI(property.getValueIRI(),RDF.TYPE,OWL.FUNCTIONALPROPERTY);
        if(isFunctionalProperty) {
            property.setIsFunctional(true);
        }
    }

    public void mapInverseFunctionalProperties(ClassRepresentation classRep,PropertyRepresentation property){
        boolean isInverseFunctionalProperty = modelManager.existStatementWithIRI(property.getValueIRI(),RDF.TYPE,OWL.INVERSEFUNCTIONALPROPERTY);
        if(isInverseFunctionalProperty) {
            PropertyRepresentation inverseProperty = createInverseProperty(classRep, property, Values.iri(classRep.getNamespace() + property.getRangeClass().getName().toLowerCase()));
            inverseProperty.setIsFunctional(true);
        }
    }


    public void mapInverseProperties(ClassRepresentation classRep, PropertyRepresentation property){
        ClassRepresentation rangeClass = property.getRangeClass();
        Set<IRI> inverseIRIProperties = modelManager.getAllIRISubjects(OWL.INVERSEOF, property.getValueIRI());
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
        Set<IRI> equivalentProperties = modelManager.getAllIRISubjects(OWL.EQUIVALENTPROPERTY, property.getValueIRI());
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

    public void mapImports(OntologyRepresentation ontology){
        Set<IRI> imports = modelManager.getAllIRIObjects(IMPORTS_IRIS,ontology.getValueIRI());
        for(IRI importVal : imports){
            ontology.addImports(importVal.stringValue());
        }
    }

    public void mapPriorVersion(OntologyRepresentation ontology){
        IRI priorVersion = modelManager.getFirstIRIObject(PRIORVERSION_IRIS,ontology.getValueIRI());
        if ( priorVersion!= null ){
            ontology.setPriorVersion(priorVersion.stringValue());
        }
    }

    public void mapOntologyInformations(OntologyRepresentation ontology){
        mapImports(ontology);
        mapPriorVersion(ontology);
    }

    public OntologyRepresentation getOWLOntology(){
        IRI owlOntologyIRI = modelManager.getFirstIRISubject(RDF.TYPE,OWL.ONTOLOGY);
        OntologyRepresentation onto = new OntologyRepresentation(owlOntologyIRI.getNamespace(),owlOntologyIRI.getLocalName());
        return onto;

    }

}

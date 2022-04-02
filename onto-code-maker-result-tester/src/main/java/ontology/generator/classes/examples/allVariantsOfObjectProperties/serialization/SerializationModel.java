
package ontology.generator.classes.examples.allVariantsOfObjectProperties.serialization;

import org.eclipse.rdf4j.model.*;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

import java.util.stream.Collectors;
import ontology.generator.classes.examples.allVariantsOfObjectProperties.entities.*;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.RDFCollections;


public abstract class SerializationModel<T> {

    abstract public void addToModel(Model model, T entity);

    abstract public T getInstanceFromModel(Model model, IRI name, int nestingLevel)throws Exception;

    abstract public Collection<T> getAllInstancesFromModel(Model model,int nestingLevel) throws Exception;

    abstract public void removeInstanceFromModel(Model model, IRI name);

    abstract public void updateInstanceInModel(Model model, T entity);

    public Literal getFirstLiteralObject(Model model, IRI predicate, IRI instanceIri){
            Set<Value> objects = model.filter(instanceIri,predicate,null).objects();
            for(Value object : objects){
                if(object.isLiteral()){
                    return (Literal) object;
                }
            }
            return null;
     }

    public Set<Literal> getAllLiteralObjects(Model model,IRI predicate, IRI subject){
             Set<Literal> allObjectsIRIs = new HashSet<>();
             Set<Value> objects = getAllObjects(model,predicate,subject);
             for(Value object:objects){
                 if(object.isLiteral()){
                     allObjectsIRIs.add((Literal)object);
                 }
             }
             return allObjectsIRIs;
    }

    public IRI getFirstIriObject(Model model,IRI predicate, IRI instanceIri){
             Set<Value> objects = model.filter(instanceIri,predicate,null).objects();
             for(Value object : objects){
                 if(object.isIRI()){
                     return (IRI) object;
                 }
             }
             return null;
    }

    public Set<IRI> getAllIRIObjects(Model model,IRI predicate, IRI subject){
             Set<IRI> allObjectsIRIs = new HashSet<>();
             Set<Value> objects = getAllObjects(model,predicate,subject);
             for(Value object:objects){
                 if(object.isIRI()){
                     allObjectsIRIs.add((IRI) object);
                 }
             }
             return allObjectsIRIs;
    }

    public Set<Value> getAllObjects(Model model,IRI predicate, IRI subject){
        return model.filter(subject, predicate, null).objects();
      }

      public IRI getFirstIRISubject(Model model,IRI predicate, Value object){
              Set<Resource> subjects = getAllSubjects(model,predicate,object);
              for(Resource subject : subjects){
                  if(subject.isIRI()){
                      return (IRI) subject;
                  }
              }
              return null;
          }

    public Set<Resource> getAllSubjects(Model model,IRI predicate, Value object){
        return model.filter(null, predicate, object).subjects();
    }

     public Set<IRI> getAllIRISubjects(Model model,IRI predicate, Value object){
        Set<IRI> allSubjectsIRIs = new HashSet<>();
        Set<Resource> subjects = getAllSubjects(model,predicate,object);
        for(Resource subject:subjects){
            if(subject.isIRI()){
                allSubjectsIRIs.add((IRI) subject);
            }
        }
        return allSubjectsIRIs;
     }

     public Set<Resource> getAllResourceObjects(Model model,IRI predicate, IRI subject){
             Set<Resource> allObjectsIRIs = new HashSet<>();
             Set<Value> objects = getAllObjects(model,predicate,subject);
             for(Value object:objects){
                 if(object.isResource()){
                     allObjectsIRIs.add((Resource)object);
                 }
             }
             return allObjectsIRIs;
     }

     public Model getModelRDFCollection(Model model,Resource node){
        return RDFCollections.getCollection(model, node, new LinkedHashModel());
     }

    public List<Value> getRDFCollection(Model model,Resource node){
        List<Value> retValues = new ArrayList<>();
        RDFCollections.asValues(model, node, retValues);
        return retValues;
    }

    public void setRDFCollection(Model model,IRI subject,IRI predicate,List<OntoEntity> values){
        List<IRI> irisValues = values.stream().map( entity -> entity.getIri()).collect(Collectors.toList());
        if(!irisValues.isEmpty()){
            Resource head = Values.bnode();
            model.addAll( RDFCollections.asRDF(irisValues, head, new LinkedHashModel()));
            model.add(subject, predicate, head);
        }
    }

    public void setLiteralsRDFCollection(Model model,IRI subject,IRI predicate,List<Object> values){
            List<Literal> literalValues = new ArrayList<>();
            values.stream().forEach(entity ->literalValues.add(Values.literal(entity)));
            if(!literalValues.isEmpty()){
                Resource head = Values.bnode();
                model.addAll( RDFCollections.asRDF(literalValues, head, new LinkedHashModel()));
                model.add(subject, predicate, head);
            }
    }

    public IRI getSubjectOfCollectionValue(Model model,IRI predicate,Value object){
            if( model.contains(null,predicate,object)){
                return getFirstIRISubject(model,predicate,object);
            }else {
                Set<Resource> objs = model.filter(null, null, object).subjects();
                for (Resource obj : objs) {
                    if (obj.isBNode() && model.contains(null, predicate, obj)) {
                        return getFirstIRISubject(model, predicate, obj);
                    }else if(obj.isBNode()){
                        IRI retVal = getSubjectOfCollectionValue(model, predicate, obj);
                        if(retVal != null){
                            return retVal;
                        }
                    }
                }
            }

            return null;
        }


}


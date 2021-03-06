package ontology.tool.mapper;

import org.eclipse.rdf4j.model.*;
import org.eclipse.rdf4j.model.impl.LinkedHashModel;
import org.eclipse.rdf4j.model.util.RDFCollections;
import org.eclipse.rdf4j.model.util.Values;
import org.eclipse.rdf4j.model.vocabulary.RDF;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/***
 *  ModelManager.java
 *
 *  A class for retrieving data from the model.
 *  This class allow some methods to retrieve data from RDF model.
 *
 *  @author Tomas Svetlik
 *  2022
 *
 *  OntoCodeMaker
 **/
public class ModelManager {
    private Model model;

    public ModelManager(Model model){
        this.model = model;
    }

    public Literal getFirstLiteralObject(List<IRI> predicates, Resource subject){
        for(IRI predicate:predicates){
            Literal literal = getFirstLiteralObject(predicate,subject);
            if(literal != null){
                return literal;
            }
        }
        return null;
    }

    public Literal getFirstLiteralObject(IRI predicate, Resource subject){
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object : objects){
            if(object.isLiteral()){
                return (Literal) object;
            }
        }
        return null;
    }

    public IRI getFirstIRIObject(List<IRI> predicates, Resource subject){
        for(IRI predicate:predicates){
            IRI iri = getFirstIRIObject(predicate,subject);
            if(iri != null){
                return iri;
            }
        }
        return null;
    }

    public IRI getFirstIRIObject(IRI predicate, Resource subject){
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object : objects){
            if(object.isIRI()){
                return (IRI) object;
            }
        }
        return null;
    }

    public IRI getFirstIRISubject(List<IRI> predicates, Value object){
        for(IRI predicate:predicates){
            IRI iri = getFirstIRISubject(predicate,object);
            if(iri != null){
                return iri;
            }
        }
        return null;
    }


    public IRI getFirstIRISubject(IRI predicate, Value object){
        Set<Resource> subjects = getAllSubjects(predicate,object);
        for(Resource subject : subjects){
            if(subject.isIRI()){
                return (IRI) subject;
            }
        }
        return null;
    }

    public Set<Literal> getAllLiteralObjects(List<IRI> predicates, Resource subject){
        Set<Literal> allObjectLiterals = new HashSet<>();
        for(IRI predicate:predicates){
            Set<Literal> objects = getAllLiteralObjects(predicate,subject);
            allObjectLiterals.addAll(objects);
        }
        return allObjectLiterals;
    }

    public Set<IRI> getAllIRIObjects(List<IRI> predicates, Resource subject){
        Set<IRI> allObjectLiterals = new HashSet<>();

        for(IRI predicate:predicates){
            Set<IRI> objects = getAllIRIObjects(predicate,subject);
            allObjectLiterals.addAll(objects);

        }
        return allObjectLiterals;
    }

    public Set<Literal> getAllLiteralObjects(IRI predicate, Resource subject){
        Set<Literal> allObjectsLiterals = new HashSet<>();
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object:objects){
            if(object.isLiteral()){
                allObjectsLiterals.add((Literal) object);
            }
        }
        return allObjectsLiterals;
    }

    public Set<IRI> getAllIRIObjects(IRI predicate, Resource subject){
        Set<IRI> allObjectsIRIs = new HashSet<>();
        Set<Value> objects = getAllObjects(predicate,subject);
        for(Value object:objects){
            if(object.isIRI()){
                allObjectsIRIs.add((IRI) object);
            }
        }
        return allObjectsIRIs;
    }

    public Set<Value> getAllObjects(IRI predicate, Resource subject){
        return model.filter(subject, predicate, null).objects();
    }

    public Set<Value> getAllObjects(List<IRI> predicates, Resource subject){
        Set<Value> allObjects = new HashSet<>();
        for(IRI predicate:predicates){
            Set<Value> objects=  model.filter(subject, predicate, null).objects();
            allObjects.addAll(objects);
        }
        return allObjects;
    }

    public Set<IRI> getAllIRISubjects(List<IRI> predicate, Resource object){
        Set<IRI> allSubjectsIRI = new HashSet<>();

        for(IRI predIRI:predicate){
            Set<IRI> subjects = getAllIRISubjects(predIRI,object);
            allSubjectsIRI.addAll(subjects);
        }
        return allSubjectsIRI;
    }

    public Set<IRI> getAllIRISubjects(IRI predicate, Resource object){
        Set<IRI> allSubjectsIRIs = new HashSet<>();
        Set<Resource> subjects = getAllSubjects(predicate,object);
        for(Resource subject:subjects){
            if(subject.isIRI()){
                allSubjectsIRIs.add((IRI) subject);
            }
        }
        return allSubjectsIRIs;
    }

    public Set<IRI> getAllIRISubjects(IRI predicate, List<Resource> objects){
        Set<IRI> allSubjectsIRIs = new HashSet<>();
        Set<Resource> subjects = new HashSet<>();
        for(Resource object:objects){
            subjects.addAll(getAllSubjects(predicate,object));
        }

        for(Resource subject:subjects){
            if(subject.isIRI()){
                allSubjectsIRIs.add((IRI) subject);
            }
        }
        return allSubjectsIRIs;
    }


    public Set<Resource> getAllSubjects(IRI predicate, Value object){
        return model.filter(null, predicate, object).subjects();
    }

    public Set<Resource> getAllSubjects(List<IRI> predicates, Resource object){
        Set<Resource> allSubjects = new HashSet<>();
        for(IRI predicate:predicates){
            Set<Resource> objects=  getAllSubjects(predicate,object);
            allSubjects.addAll(objects);
        }
        return allSubjects;
    }

    public Resource getFirstSubject(IRI predicate, Resource object){
        Set<Resource>subjects= model.filter(null, predicate, object).subjects();
        return subjects.size() > 0 ? subjects.iterator().next() : null;
    }

    public Resource getFirstResource(IRI predicate, Resource subject){
        Set<Value>objects= model.filter(subject, predicate, null).objects();
        for(Value object:objects){
            if(object.isResource()){
                return (Resource) object;
            }
        }
        return null;
    }


    public boolean existStatementWithIRI(Resource subject,IRI predicate,Value object){
        Model resultModel =  model.filter(subject, predicate, object);
        return resultModel.size() > 0;
    }

    public Statement getStatementWithIRI(Resource subject,IRI predicate,Value object){
        Iterable<Statement> stat = model.getStatements(subject, predicate, object);
        return stat.iterator().hasNext()? stat.iterator().next():null;
    }

    public Statement getFirstStatementWithIRI(Resource subject,List<IRI> predicates,Value object){
        Statement res = null;
        for(IRI predicate:predicates){
            res = getStatementWithIRI(subject, predicate, object);
            if(res != null){
                break;
            }
        }
        return res;
    }

    public boolean isCollection(Resource subject){
        return existStatementWithIRI(subject, RDF.FIRST,null);
    }

    public Set<Value> getRDFCollection(Resource node){
        Set<Value> retValues = new HashSet<>();
        RDFCollections.asValues(model, node, retValues);
        return retValues;
    }

    public void setRDFCollection(IRI subject,IRI predicate,List<IRI> values){
        Resource head = Values.bnode();
        model.addAll( RDFCollections.asRDF(values, head, new LinkedHashModel()));
        model.add(subject, predicate, head);
    }

    public IRI getSubjectOfValue(IRI predicate,Value object){
        if( model.contains(null,predicate,object)){
            return getFirstIRISubject(predicate,object);
        }else {
            Set<Resource> objs = model.filter(null, null, object).subjects();
            for (Resource obj : objs) {
                if (obj.isBNode() && model.contains(null, predicate, obj)) {
                    return getFirstIRISubject(predicate, obj);
                }else if(obj.isBNode()){
                    IRI retVal = getSubjectOfValue( predicate, obj);
                    if(retVal != null){
                        return retVal;
                    }
                }
            }
        }

        return null;
    }

}

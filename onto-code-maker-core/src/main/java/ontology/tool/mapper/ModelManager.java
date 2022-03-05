package ontology.tool.mapper;

import org.eclipse.rdf4j.model.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ModelManager {
    private Model model;

    public ModelManager(Model model){
        this.model = model;
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
}

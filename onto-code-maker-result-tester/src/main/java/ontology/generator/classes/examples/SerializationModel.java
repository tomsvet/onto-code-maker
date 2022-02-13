package ontology.generator.classes.examples;

import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Model;

import java.util.Collection;

public interface SerializationModel<T> {

    public void addToModel(Model model, T entity);

    public T getInstanceFromModel(Model model, IRI name);

    public Collection<T> getAllInstancesFromModel(Model model);

    public void removeInstanceFromModel(Model model, IRI name);

}

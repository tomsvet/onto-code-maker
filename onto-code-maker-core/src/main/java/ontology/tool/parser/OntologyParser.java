package ontology.tool.parser;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.Model;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.Rio;
import org.junit.platform.commons.logging.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class OntologyParser {

    private static final Logger logger = LogManager.getLogger(OntologyParser.class);

    public Model parseOntology(String[] inputFiles,String formatName) throws FileNotFoundException {
        
        logger.trace("Starting parser");

        Model model = null;

        for(String filename: inputFiles){

            Path file = Paths.get(filename);
            if (!Files.exists(file)){
                throw new FileNotFoundException(filename);
            }

            logger.debug("Parsing file: " + filename);

            RDFFormat format;

            if(formatName.isEmpty()){
                format = Rio.getParserFormatForFileName(file.getFileName().toString()).orElse(RDFFormat.RDFXML);
            }else{
                format = Rio.getParserFormatForMIMEType(formatName).orElse(RDFFormat.RDFXML);
            }

            try {
                InputStream inputStream = Files.newInputStream(file);

                Model newModel = Rio.parse(inputStream, "", format);

                if (model == null){
                    model= newModel;
                }else{
                    model.addAll(newModel);
                }
                logger.debug("File " + filename + " is loaded in the model.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return model;
    }

}

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

    public Model parseOntology(String[] inputFiles, String formatName) throws Exception {

        logger.trace("Starting parser");

        Model model = null;

        for (String filename : inputFiles) {

            Path file = Paths.get(filename);
            if (!Files.exists(file)) {
                throw new FileNotFoundException(filename);
            }

            logger.debug("Parsing file: " + filename);

            RDFFormat format;

            if (formatName.isEmpty()) {
                format = Rio.getParserFormatForFileName(file.getFileName().toString()).orElse(RDFFormat.RDFXML);
            } else {
                format = getRDFFormat(formatName);
                if(format == null){
                    throw new Exception("Not supported format name. Check help see supported names.");
                }
            }

            try {
                InputStream inputStream = Files.newInputStream(file);

                Model newModel = Rio.parse(inputStream, "", format);

                if (model == null) {
                    model = newModel;
                } else {
                    model.addAll(newModel);
                }
                logger.debug("File " + filename + " is loaded in the model.");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return model;
    }

    private RDFFormat getRDFFormat(String formatName) {
        if(formatName == null ){
            return null;
        }else if (formatName.equalsIgnoreCase("turtle")) {
            return RDFFormat.TURTLE;
        } else if (formatName.equalsIgnoreCase("n-quads")) {
            return RDFFormat.NQUADS;
        } else if (formatName.equalsIgnoreCase("rdf/xml")) {
            return RDFFormat.RDFXML;
        } else if (formatName.equalsIgnoreCase("xml")) {
            return RDFFormat.RDFXML;
        } else if (formatName.equalsIgnoreCase("json-ld")) {
            return RDFFormat.JSONLD;
        } else if (formatName.equalsIgnoreCase("n-triples")) {
            return RDFFormat.NTRIPLES;
        }else if (formatName.equalsIgnoreCase("trix")) {
            return RDFFormat.TRIX;
        }else if (formatName.equalsIgnoreCase("trig")) {
            return RDFFormat.TRIG;
        }else if (formatName.equalsIgnoreCase("rdf/json")) {
            return RDFFormat.RDFJSON;
        }else {
            return Rio.getParserFormatForMIMEType(formatName).orElse(null);
        }
    }

}

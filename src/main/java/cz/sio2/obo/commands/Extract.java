package cz.sio2.obo.commands;

import cz.sio2.obo.OBOFoundryVersionExtractor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "extract",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Extracts the latest versions of ontologies."
)
@Slf4j
class Extract implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", defaultValue = "https://obofoundry.org/registry/ontologies.ttl", description = "The IRI to check out")
    private String iri;

    @CommandLine.Option(names = {"-h"}, defaultValue = "4096", description = "Header size to be fetched.")
    private int headerSize;

    @CommandLine.Option(names = {"-o"}, description = "Output file.")
    private String outputFile;

    @Override
    public Integer call() {
        try {
            new OBOFoundryVersionExtractor().extract(iri, outputFile, headerSize);
        } catch (Exception e) {
            log.error("Error during extraction: ", e);
            return -1;
        }
        return 0;
    }
}
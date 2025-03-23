package cz.sio2.ontology.version.commands;

import cz.sio2.ontology.version.model.Configuration;
import cz.sio2.ontology.version.model.Transformer;
import cz.sio2.ontology.version.model.Utils;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.net.URL;
import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "transform",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Extracts the latest versions of ontologies."
)
@Slf4j
class Transform implements Callable<Integer> {

    @CommandLine.Parameters(index = "0", description = "The configuration file URL.")
    private URL configurationUrl;

    @CommandLine.Option(names = {"-i"}, description = "Input file with the extracted versions. If the filename ends with 'HTML' a HTML report is generated, if not then a CSV report is generated.")
    private String inputFile;

    @CommandLine.Option(names = {"-o"}, description = "Output file to store the extracted version to.")
    private String outputFile;

    @Override
    public Integer call() {
        try {
            final Configuration  configuration  = Utils.loadConfiguration(configurationUrl);
            if (outputFile.endsWith("html")) {
                new Transformer().transformToHtml(inputFile, outputFile, configuration);
            } else {
                new Transformer().transformToCsv(inputFile, outputFile, configuration);
            }
        } catch (Exception e) {
            log.error("Error during extraction: ", e);
            return -1;
        }
        return 0;
    }
}
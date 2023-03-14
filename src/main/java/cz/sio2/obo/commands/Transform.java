package cz.sio2.obo.commands;

import cz.sio2.obo.OBOFoundryHeaderExtractor;
import lombok.extern.slf4j.Slf4j;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(
        name = "transform",
        mixinStandardHelpOptions = true,
        version = "1.0",
        description = "Extracts the latest versions of ontologies."
)
@Slf4j
class Transform implements Callable<Integer> {

    @CommandLine.Option(names = {"-i"}, description = "Input file with the extracted versions. If the filename ends with 'HTML' a HTML report is generated, if not then a CSV report is generated.")
    private String inputFile;

    @CommandLine.Option(names = {"-o"}, description = "Output file to store the extracted version to.")
    private String outputFile;

    @Override
    public Integer call() {
        try {
            if (outputFile.endsWith("html")) {
                new OBOFoundryHeaderExtractor().transformToHtml(inputFile, outputFile);
            } else {
                new OBOFoundryHeaderExtractor().transformToCsv(inputFile, outputFile);
            }
        } catch (Exception e) {
            log.error("Error during extraction: ", e);
            return -1;
        }
        return 0;
    }
}
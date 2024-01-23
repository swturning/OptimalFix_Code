package my.smartdec.detect.app;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 */
public final class ReportDefault implements Report {

    /**
     *
     */
    private final DirectoryAnalysis analysis;
    /**
     *
     */
    private final Media media;

    /**
     * @param da directory analysis
     * @param md media
     */
    public ReportDefault(final DirectoryAnalysis da, final Media md) {
        this.analysis = da;
        this.media = md;
    }

    @Override
    public void print() throws Exception {
        Stream<DirectoryAnalysis.Info> stream = this.analysis.stream();


        List<DirectoryAnalysis.Info> infoList = stream.sorted().collect(Collectors.toList());

        for(DirectoryAnalysis.Info info : infoList){
            this.media.accept(info);
        }

//        stream.forEach(this.media::accept);

       // this.analysis.stream().forEach(this.media::accept);
    }
}

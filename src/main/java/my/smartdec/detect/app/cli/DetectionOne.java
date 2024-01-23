package my.smartdec.detect.app.cli;


import com.sun.org.apache.xpath.internal.SourceTree;
import com.sun.org.apache.xpath.internal.operations.Bool;
import my.smartdec.detect.*;
import my.smartdec.detect.app.DirectoryAnalysis;
import my.smartdec.detect.app.DirectoryAnalysisCombined;
import my.smartdec.detect.app.DirectoryAnalysisDefault;
import my.smartdec.detect.app.ReportDefault;
import my.smartdec.detect.app.SourceLanguage;
import my.smartdec.detect.app.SourceLanguages;
import my.smartdec.detect.app.TreeFactoryDefault;
import my.smartdec.detect.repaire.AutoRepaire;
import my.smartdec.detect.repaire.PatternRepaire;
import my.smartdec.detect.repaire.entity.CountPattern;
import my.smartdec.detect.repaire.entity.ErrorReport;
import my.smartdec.detect.repaire.entity.RepaireDetail;
import my.smartdec.detect.repaire.entity.RepaireResult;
import my.smartdec.detect.repaire.seconDection.DectionTwice;
import my.smartdec.detect.repaire.util.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.script.ScriptException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;
import java.net.URI;
import java.nio.file.*;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

/**
         *
         */     
public final class DetectionOne {       

    public static void main(final String... args) throws Exception {
        DetectionOne.main(new ArgumentsDefault(args));
    }

    public static void main(final Arguments arguments) throws Exception {
        Optional<String> value = arguments.value("-p", "--path");
        String stringValue = null;
        if (value.isPresent()) {
            stringValue = value.get();
            PropertiesUtil.init(stringValue);
           // System.out.println(stringValue);
        }
        FileUtil.RemoveEmptyLinesAndComments(PropertiesUtil.contractFile);

        ProjectUtil.prettier(PropertiesUtil.contractFile);

        FileUtil.RemoveEmptyLinesAndComments(PropertiesUtil.contractFile);

        System.out.println("Originate contract:");
        System.out.println();

        FileUtil.readFileContent(PropertiesUtil.fileDe);
        Path src = arguments
                .value("-p", "--path")
                .map(Paths::get)
                .filter(Files::exists)
                .orElseThrow(IllegalArgumentException::new);

        Function<SourceLanguage, RulesXml.Source> defaultRules =
                sourceLanguage -> () -> {
            String rulesFileName = sourceLanguage.rulesFileName();
            URI uri = RulesXml
                    .class
                    .getResource(rulesFileName)
                    .toURI();


            return Paths.get(uri);
        };


        Function<SourceLanguage, RulesXml.Source> rules = arguments
                .value("-r", "--rules")
                .map(Paths::get)
                .filter(Files::isRegularFile)
                .<Function<SourceLanguage, RulesXml.Source>>
                        map(path -> language -> () -> path)
                .orElse(defaultRules);

        new DetectionOne(src, rules).run();
    }

    /**
     *
     */
    private final Path source;

    public static int IntergerVul = 0;



    public List<ErrorReport> errorReports ;

    private final Function<
            ? super SourceLanguage,
            ? extends RulesXml.Source
    > rules;


    public DetectionOne(
            final Path src,
            final Function<
                    ? super SourceLanguage,
                    ? extends RulesXml.Source
            > rs
    ) {
        this.source = src;
        this.rules = rs;
    }

    private DirectoryAnalysis makeDirectoryAnalysis(
            final SourceLanguage sourceLanguage
    ) throws Exception {
        //DocumentBuilder 是用于解析和操作 XML 文档的类，它允许你将 XML 数据加载到内存中，以便进行处理。
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        ////rules.apply(sourceLanguage) 的作用是将 sourceLanguage 参数传递给 rules 函数，并获取返回的结果，该结果的类型是 RulesXml.Source
        RulesXml.Source apply = this.rules.apply(sourceLanguage);

        //建一个XPath对象，以便对XML文档执行XPath查询
        XPath xPath = XPathFactory.newInstance().newXPath();

        RulesXml rulesXml = new RulesXml(apply, xPath, Throwable::printStackTrace);

        RulesCached rulesCached = new RulesCached(rulesXml);
        DirectoryAnalysisDefault directoryAnalysisDefault = new DirectoryAnalysisDefault(
                this.source,
                p -> p.toString().endsWith(sourceLanguage.fileExtension()),
                new TreeFactoryDefault(
//                        DocumentBuilderFactory
//                                .newInstance()
//                                .newDocumentBuilder()
                        documentBuilder,
                        sourceLanguage
                ),
                rulesCached
        );
        return directoryAnalysisDefault;

//        return new DirectoryAnalysisDefault(
//                this.source,
//                p -> p.toString().endsWith(sourceLanguage.fileExtension()),
//                new TreeFactoryDefault(
////                        DocumentBuilderFactory
////                                .newInstance()
////                                .newDocumentBuilder()
//                                documentBuilder,
//                        sourceLanguage
//                ),
//                new RulesCached(
//                        new RulesXml(
//
////                                this.rules.apply(sourceLanguage),
//                                apply,
////                                XPathFactory.newInstance().newXPath(),
//                                xPath,
//                                //于将异常的跟踪信息打印到标准错误流。
//                                Throwable::printStackTrace
//                        )
//                )
//        );

    }

    /**
     * @throws Exception exception
     */
    public void run() throws Exception {
        long startTime = System.currentTimeMillis();
        Boolean flag = true;

        SourceLanguages.Solidity soliditySource = new SourceLanguages.Solidity();
        SourceLanguages.Vyper vyperSource = new SourceLanguages.Vyper();

        DirectoryAnalysisDefault directoryAnalysisSolidity = (DirectoryAnalysisDefault) makeDirectoryAnalysis(soliditySource);
        //System.out.println("sssws"+JSON.toJSONString(directoryAnalysisSolidity));
        DirectoryAnalysisDefault directoryAnalysisVyper = (DirectoryAnalysisDefault) makeDirectoryAnalysis(vyperSource);

        DirectoryAnalysisCombined directoryAnalysisCombined = new DirectoryAnalysisCombined(directoryAnalysisSolidity, directoryAnalysisVyper);
        //System.out.println("directoryAnalysisCombined  "+JSON.toJSONString(directoryAnalysisCombined));
//        DirectoryAnalysisCombined directoryAnalysisCombined = new DirectoryAnalysisCombined(
//                makeDirectoryAnalysis(new SourceLanguages.Solidity()),
//                makeDirectoryAnalysis(new SourceLanguages.Vyper())
//        );
        List<ErrorReport> errorReportList = new ArrayList<>();

        CountPattern countPattern = new CountPattern();
        Map<String, Integer> result = new HashMap<>();
        new ReportDefault(
//                new DirectoryAnalysisCombined(
//                    makeDirectoryAnalysis(new SourceLanguages.Solidity()),
//                    makeDirectoryAnalysis(new SourceLanguages.Vyper())
//                ),
                directoryAnalysisCombined,
                info -> {

                    info.treeReport().streamUnchecked().forEach(
                            tree -> tree.contexts().forEach(


                                    context -> {
                                        int line = context.getStart().getLine();
                                        int line1 = context.getStop().getLine();
                                        String text = context.getText();
                                        //word(text,line,context,tree);
//                                        System.out.printf(
//                                                "规则: %s "
//                                                      + "模式: %s "
//                                                      + "风险等级: %d   "
//                                                      + "语言: Solidity "
//                                                      + "行: %d  结束行：%d  列: %d   "
//                                                      + "代码内容: %s%n%n",
//                                                tree.rule().id(),
//                                                tree.pattern().id(),
//                                                tree.pattern().severity(),
//                                                line,
//                                                line1,
//                                                context
//                                                       .getStart()
//                                                       .getCharPositionInLine(),
//                                                text
//                                        );
                                        int a = getPartternIndex(countPattern,tree.pattern().id());



                                        if (a!=-1){
                                            try {
                                                errorReportList.add(new ErrorReport(a,tree.pattern().id(),tree.pattern().severity(),line,line1,text, NodeUtil.getAllCopy(tree.pattern().id(),a)));
                                            } catch (XPathExpressionException e) {
                                                e.printStackTrace();
                                            }
                                        }

                                        //System.out.println();

//                                        System.out.println("hahahahahahah"+tree.pattern().id());
//                                        DectionTwice dectionTwice =new DectionTwice(context.getStart().getLine(),context.getStop().getLine());
//                                        dectionTwice.reetrancy();

                                        result.compute(
                                                tree.rule().id(),
                                                (k, v) -> Optional
                                                        .ofNullable(v)
                                                        .map(i -> i + 1)
                                                        .orElse(1)
                                        );
                                    }
                            )
                    );
                    // 统计违反规则

                    result.forEach((k, v) -> System.out.println(k + " :" + v));
                }
        ).print();

        if (result.containsKey("SOLIDITY_IntegerOverflow")) {
            IntergerVul = result.get("SOLIDITY_IntegerOverflow");
        }


        //System.out.println(errorReportList);
        DectionSecond(errorReportList);
        long deTime = System.currentTimeMillis();

        //构造整个语法树的 深度拷贝
        if (flag&&errorReportList.size()>0){
            ErrorReport errorReport = errorReportList.get(0);
            Node sourceUnitNode = NodeUtil.sourceUnitNode(errorReport.getPattern(), errorReport.getIndex());
            //assert sourceUnitNode != null;
            NodeCopyUtil.sourceNode = sourceUnitNode.cloneNode(true);
           // System.out.println(NodeUtil.getNodeTextContent( NodeCopyUtil.sourceNode));

        }

        //输出错误内容
        //writeErrorToFile(errorReportList);
        List<RepaireDetail> detaiList = new ArrayList<>();
        PatternRepaire patternRepaire = new PatternRepaire();
        Node sourceNode = null;
        for (ErrorReport er :errorReportList){

            if (!er.isDectiontwic()){

                List<RepaireDetail> repaireResult = patternRepaire.getRepaireResult(er);
                if (repaireResult!=null){
                    detaiList.addAll(repaireResult) ;
                }
            }
            if (er.getSourceNode() !=null&&sourceNode == null){
                sourceNode = er.getSourceNode();
            }

        }
        System.out.println("total result:");
        if (errorReportList.size()==0){
            System.out.println("No Vuls are detected");
        }
        for (ErrorReport er:errorReportList){
            if (!er.isDectiontwic()){
                System.out.printf(

                        "pattern: %s "
                                + "level: %d   "
                                + " Solidity "
                                + "startLine: %d  stopLine: %d   "
                                + "context: %s%n%n",
                        er.getPattern(),
                        er.getLevel(),
                        er.getStartLine(),
                        er.getStopLine(),
                        er.getContext()
                );
            }

        }

        Collections.sort(detaiList);


        //输入修复脚本内容
//        writeReoaireToFile(detaiList);

        long endTime = System.currentTimeMillis();
        long elapsedTime = endTime - deTime;

        //自动修复
        AutoRepaire autoRepaire =new AutoRepaire();
        autoRepaire.autoRepaire(detaiList,sourceNode);
        if (ProjectUtil.i2!=null){
            elapsedTime+=ProjectUtil.i2;
        }
        if (ProjectUtil.i3!=null){
            elapsedTime+=ProjectUtil.i3;
        }
        System.out.println();
        System.out.println("Detecting Time:"+(deTime-startTime));
        System.out.println();
        System.out.println("Patch time：" + elapsedTime);
        System.out.println();
        System.out.println("Patched contract:");
        System.out.println();
        FileUtil.readFileContent("test/Patched/"+PropertiesUtil.fixedFileName);
        System.out.println();
        long programe = System.currentTimeMillis();
        long elsss = programe - startTime;


        if (ifAllFalse(errorReportList)){
            FileUtil.appendToFile("test/time/de.txt", String.valueOf((deTime-startTime)));
            FileUtil.appendToFile("test/time/pa.txt", String.valueOf(elapsedTime));
        }



    }

    private void DectionSecond(List<ErrorReport> list){
        for (ErrorReport er :list){
            if (ifCanrepaire(er.getPattern())){
                switch (er.getPattern()){
                    case "ree003":
                    case ("ree002"):
                    case "ree001": {
                        DectionTwice dectionTwice =new DectionTwice(er.getStartLine(),er.getStopLine());
                        try {
                            boolean b = dectionTwice.reetrancyDection(er);
                            er.setDectiontwic(b);
//                            if(b){
//                                list.remove(er);
//                            }

                            //System.out.println("b+"+b);
                            break;
                        } catch (XPathExpressionException | ScriptException | NoSuchFieldException | IllegalAccessException | IOException | ParserConfigurationException | SAXException e) {
                            e.printStackTrace();
                        }
                        break;
                    }

                }

            }
        }
    }


    private Boolean ifAllFalse(List<ErrorReport> list){
        if (list.size() == 0){
            return false;
        }
        for (ErrorReport er:list){
            if (!er.isDectiontwic()){
                return true;
            }
        }
        return false;
    }

    private Boolean ifCanrepaire(String s){
        if (s.equals("ree001")||s.equals("sdm001")||s.equals("ree002")||s.equals("ree003")){
            return true;
        }

        return false;
    }

    private int getPartternIndex(CountPattern countPattern,String parttern){
        if (parttern.equals("sdm001")){
            int temp = countPattern.getSdm001();
            temp +=1;
            countPattern.setSdm001(temp);
            return temp;
        }else if (parttern.equals("ree001")){
            int temp = countPattern.getRee001();
            temp +=1;
            countPattern.setRee001(temp);
            return temp;
        }else if (parttern.equals("SIO001")){
            int temp = countPattern.getSIO001();
            temp +=1;
            countPattern.setSIO001(temp);
            return temp;

        }else if (parttern.equals("SIO002")){
            int temp = countPattern.getSIO002();
            temp +=1;
            countPattern.setSIO002(temp);
            return temp;

        }else if (parttern.equals("SIO003")){
            int temp = countPattern.getSIO003();
            temp +=1;
            countPattern.setSIO003(temp);
            return temp;

        }else if (parttern.equals("SIO004")){
            int temp = countPattern.getSIO004();
            temp +=1;
            countPattern.setSIO004(temp);
            return temp;

        }else if (parttern.equals("sto001")){
            int temp = countPattern.getSto001();
            temp +=1;
            countPattern.setSto001(temp);
            return temp;

        }else if (parttern.equals("sto002")){
            int temp = countPattern.getSto002();
            temp +=1;
            countPattern.setSto002(temp);
            return temp;

        }else if (parttern.equals("suc001")){
            int temp = countPattern.getSuc001();
            temp +=1;
            countPattern.setSuc001(temp);
            return temp;

        }else if(parttern.equals("suc003")){
            int temp = countPattern.getSuc003();
            temp +=1;
            countPattern.setSuc003(temp);
            return temp;
        }

       else if (parttern.equals("sus001")){
            int temp = countPattern.getSus001();
            temp +=1;
            countPattern.setSus001(temp);
            return temp;
        }else if (parttern.equals("ree003")){
            int temp = countPattern.getRee003();
            temp +=1;
            countPattern.setRee003(temp);
            return temp;
        }else if (parttern.equals("ree002")){
            int temp = countPattern.getRee002();
            temp +=1;
            countPattern.setRee002(temp);
            return temp;
        }else if (parttern.equals("nac001")){
            int temp = countPattern.getNac001();
            temp +=1;
            countPattern.setNac001(temp);
            return temp;
        }
        return -1;
    }






}

////格式化命令 npx prettier --write --plugin=prettier-plugin-solidity 'D:\DectionContract\SolidityDetection-main\test\SimpleDao.sol'
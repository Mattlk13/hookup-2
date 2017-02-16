package rs.hookupspring.springweb.services;

import rs.hookupspring.entity.User;
import weka.classifiers.Classifier;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;
import java.util.logging.Logger;

/**
 * Created by Bandjur on 2/4/2017.
 */

@Service
public class WekaMiningService {


    public void Test() {

        try {
            Classifier randomForest = (Classifier) weka.core.SerializationHelper.read("E:\\!FAKS\\!MSC\\Speed hookup\\20170201-wekaSampling-metacost\\apply data\\randomForestAlmostBalanced.model");

            // TODO make test .arff file with attribute 'match' included (the attribute set should be exactly like the one in the commented section
//            Instances unlabeled = new Instances(
//                    new BufferedReader(
//                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\weka\\almost-balanced-not-normalized.arff")));

            Instances unlabeled = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\weka\\bandar_harem_dataset.arff")));

            unlabeled.insertAttributeAt(new Attribute("match",Attribute.NOMINAL), unlabeled.numAttributes());

            unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
//            unlabeled.setClassIndex(68);

            // create copy
            Instances labeled = new Instances(unlabeled);

            // label instances
            for (int i = 0; i < unlabeled.numInstances(); i++) {
                unlabeled.instance(i).classIsMissing();
                double clsLabel = randomForest.classifyInstance(unlabeled.instance(i));
                labeled.instance(i).setClassValue(clsLabel);
            }
            // save labeled data
            BufferedWriter writer = new BufferedWriter(
                    new FileWriter("E:\\!FAKS\\!MSC\\Speed hookup\\weka\\weka-classifyResults.arff"));
            writer.write(labeled.toString());
            writer.newLine();
            writer.flush();
            writer.close();

            int counter=0, paired = 0, unpaired = 0;
            for (Instance i : unlabeled)  {
                double v = randomForest.classifyInstance(i);
                if (v == 0.0) {
                    unpaired++;
                }
                else {
                    paired++;
                }
                System.out.println(++counter + " rezultat: " + v + "\n");
            }

            System.out.println("\n paired = " + paired + ", unpaired = " + unpaired);


        } catch (Exception e) {



            e.printStackTrace();
        }
    }


    public void generateWekaInstance(User male, User female) {

    }

    public String readArffData() {

        StringBuilder sb= new StringBuilder();

        try {
            Instances instances = new Instances(
                    new BufferedReader(
                            new FileReader("E:\\!FAKS\\!MSC\\Speed hookup\\20170216-final\\weka\\male-non-redudant-set.arff")));

            int a =0;
            for(Instance instance : instances) {
                a++;
//                System.out.println("*******************    " + a + "    ********************");
                log.info("*******************    " + a + "    ********************");
                sb.append("*******************    " + a + "    ********************\n");

                for(int i = 0 ; i < instance.numAttributes() - 1 ; i++) {
                    log.info("attribute index (" + i + "), attribute name (" + instance.attribute(i).name()+ "), value = " + instance.value(i) + "\n");

                    sb.append("attribute name = " + instance.attribute(i).name() + "\n");
                    sb.append("attribute index (" + i + "), value = " + instance.value(i) +"\n\n");

                }
//                System.out.println("*******************************************************");
//                log.info("*******************************************************");
                sb.append("\n*******************************************************\n");
            }

        }
        catch (Exception e) {

        }

        return sb.toString();
    }


    public void AnotherWekaTest() {

    }

}

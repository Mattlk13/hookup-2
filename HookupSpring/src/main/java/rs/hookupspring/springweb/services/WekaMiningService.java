package rs.hookupspring.springweb.services;

import weka.classifiers.Classifier;
import org.springframework.stereotype.Service;
import weka.core.Attribute;
import weka.core.Instance;
import weka.core.Instances;

import java.io.*;

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

}

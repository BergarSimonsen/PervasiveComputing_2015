import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;

import weka.classifiers.bayes.BayesNet;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;
public class Classifier {

//	private final String DATA_LOCATION = "/home/bs/itu/pervasive_computing_SPCT/PervasiveComputing_2015/MandatoryAssignment2/motion_data/use_this/";
	private final String DATA_LOCATION = "/home/bs/Desktop/csv_data/";
	private final String TEST_DATA = DATA_LOCATION + "test_data/test_data.csv";
	private final String TRAINING_DATA = DATA_LOCATION + "training_data/training_data.csv";

	private DataSource trainingSource;
	private Instances trainingInstances;
	private J48 j48;
	private BayesNet bayesNet;
	private FilteredClassifier fc;

	public Classifier() {
		init();
		//		filter();
	}

	private void init() {
		try {
			trainingSource = new DataSource(TRAINING_DATA);

			// Fetch dataset
			trainingInstances = trainingSource.getDataSet();
			trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);

			// Filter
			Remove rm = new Remove();
//			rm.setAttributeIndices("1");

			// Clasifier
//			j48 = new J48();
//			j48.setUnpruned(true);
			bayesNet = new BayesNet();

			fc = new FilteredClassifier();
			fc.setFilter(rm);
			fc.setClassifier(bayesNet);

			// train and make predictions
			fc.buildClassifier(trainingInstances);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void classify(Reader reader) {
		try {
			DataSource ds;
			Instances in;



			//			ds = new DataSource(file);
			//				in = ds.getDataSet();
			in = new Instances(reader);
			in.setClassIndex(in.numAttributes() - 1);

			for (int i = 0; i < in.numInstances(); i++) {
				double pred = fc.classifyInstance(in.instance(i));
				System.out.print("ID: " + in.instance(i).value(0));
				System.out.print(", actual: " + in.classAttribute().value((int) in.instance(i).classValue()));
				System.out.println(", predicted: " + in.classAttribute().value((int) pred));
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void classify(String file) {
		try {
			DataSource ds;
			Instances in;
			
			ds = new DataSource(file);
			if (ds != null) {
				in = ds.getDataSet();
				in.setClassIndex(in.numAttributes() - 1);
				
				System.out.println("num of instances: " + in.numInstances());

				for (int i = 0; i < in.numInstances(); i++) {
					double pred = fc.classifyInstance(in.instance(i));
					System.out.print("ID: " + in.instance(i).value(0));
					System.out.print(", actual: " + in.classAttribute().value((int) in.instance(i).classValue()));
//					System.out.println("||||----- " + in.classAttribute().value(i));
//					System.out.println(", predicted: " + in.classAttribute().value((int) pred));
					System.out.println(", predicted: " + trainingInstances.classAttribute().value((int) pred));
				}	
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("ERROR classify");
		}
	}
	
	public void classify2(String file) {
		try {
			DataSource ds = new DataSource(file);
//			Instances unlabeled = new Instances(new BufferedReader(new FileReader(file)));
			Instances unlabeled = ds.getDataSet();
			

			// set class attribute
			unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

			// create copy
			Instances labeled = new Instances(unlabeled);

			// label instances
			for (int i = 0; i < unlabeled.numInstances(); i++) {
				double clsLabel = fc.classifyInstance(unlabeled.instance(i));
				labeled.instance(i).setClassValue(clsLabel);
				System.out.println(clsLabel + " -> " + trainingInstances.classAttribute().value((int) clsLabel));
			}
			// save labeled data
//			BufferedWriter writer = new BufferedWriter(	new FileWriter("/some/where/labeled.arff"));
//			writer.write(labeled.toString());
//			writer.newLine();
//			writer.flush();
//			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		} 		
	}

//		private void filter() {
//			DataSource testSource;
//			DataSource trainingSource;
//			
//			Instances trainingInstances;
//			Instances testInstances;
//			
//			try {
//				testSource = new DataSource(TEST_DATA);
//				trainingSource = new DataSource(TRAINING_DATA);
//				
//				// Fetch datasets
//				testInstances = testSource.getDataSet();
//				trainingInstances = trainingSource.getDataSet();
//				
//				testInstances.setClassIndex(testInstances.numAttributes() - 1);
//				trainingInstances.setClassIndex(trainingInstances.numAttributes() - 1);
//				
//				// Filter
//				Remove rm = new Remove();
//				rm.setAttributeIndices("1");
//				
//				// Clasifier
//				J48 j48 = new J48();
//				j48.setUnpruned(true);
//				
//				FilteredClassifier fc = new FilteredClassifier();
//				fc.setFilter(rm);
//				fc.setClassifier(j48);
//				
//	//			 // train and make predictions
//				 fc.buildClassifier(trainingInstances);
//				 for (int i = 0; i < testInstances.numInstances(); i++) {
//				   double pred = fc.classifyInstance(testInstances.instance(i));
//				   System.out.print("ID: " + testInstances.instance(i).value(0));
//				   System.out.print(", actual: " + testInstances.classAttribute().value((int) testInstances.instance(i).classValue()));
//				   System.out.println(", predicted: " + testInstances.classAttribute().value((int) pred));
//				 }			
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//	
//		}

	//	private void init() {
	//	DataSource source;
	//	Instances data;
	//	try {
	//		source = new DataSource(TRAINING_DATA);
	//		data = source.getDataSet();
	//		
	//		// setting class attribute if the data format does not provide this information
	//		// For example, the XRFF format saves the class attribute information as well
	//		if (data.classIndex() == -1)
	//			  data.setClassIndex(data.numAttributes() - 1);
	//	} catch (Exception e) {
	//		// TODO Auto-generated catch block
	//		e.printStackTrace();
	//	}
	//}	
}




package main;

import java.awt.Dimension;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import org.apache.commons.io.IOUtils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;

import com.minhaskamal.genderRecognizer.FaceDetector;
import com.minhaskamal.genderRecognizer.weightedPixel.WeightedStandardPixelTrainer;

import net.miginfocom.swing.MigLayout;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

public class Gui extends JFrame {
	static int i = 0;
	static int countTweets = 0;
	static String imgUrl = "";
	static int allFemale = 0;
	static int allMale = 0;
	static int hours = 0;

	static ImageResizer resize;
	static String keyword = "Fashion";
	static TwitterStream twitterStream;
	static StatusListener listener;
	static FilterQuery fq;
	static ConfigurationBuilder cb;

	private static final long serialVersionUID = 7167632922308975271L;
	private JTextField tfListenFor;
	static JTextField tfTweetsPerHour;
	static JTextField tfMale;
	static JTextField tfFemale;
	static JLabel lblPicture;
	static JLabel lblPicture_1;
	static JLabel lblPicture_2;
	static JLabel lblPicture_3;
	static JLabel lblPicture_4;
	static JLabel lblPicture_5;
	static JLabel lblPicture_6;
	static JLabel lblPicture_7;
	static JLabel lblPicture_8;
	static JLabel lblPicture_9;
	static JLabel lblPictureDetail;
	static JLabel lblPicture_1Detail;
	static JLabel lblPicture_2Detail;
	static JLabel lblPicture_3Detail;
	static JLabel lblPicture_4Detail;
	static JLabel lblPicture_5Detail;
	static JLabel lblPicture_6Detail;
	static JLabel lblPicture_7Detail;
	static JLabel lblPicture_8Detail;
	static JLabel lblPicture_9Detail;

	public Gui() {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		Timer t = new Timer();
		t.schedule(new TimerTask() {

			@Override
			public void run() {
				hours++;
			}
		}, 0, TimeUnit.HOURS.toMillis(1));

		resize = new ImageResizer();

		setSize(new Dimension(570, 540));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(new MigLayout("", "[93.00][245.00,grow][grow]", "[][][][247.00][grow]"));

		JLabel lblListenFor = new JLabel("Listen for:");
		getContentPane().add(lblListenFor, "cell 0 0,alignx trailing");

		tfListenFor = new JTextField();
		tfListenFor.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent arg0) {
				if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
					countTweets = 0;
					allFemale = 0;
					allMale = 0;
					filterTwitterStream();
				}
			}
		});
		getContentPane().add(tfListenFor, "cell 1 0,growx");
		tfListenFor.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(panel, "cell 0 3 3 1,grow");
		panel.setLayout(new MigLayout("", "[100.00][100.00][100.00][100.00][100.00]", "[][100.00][][101.00,grow][]"));

		JLabel lblLastProcessed = new JLabel("Last 10 processed pictures...");
		lblLastProcessed.setBorder(new BevelBorder(BevelBorder.RAISED, null, null, null, null));
		panel.add(lblLastProcessed, "cell 0 0 2 1");

		lblPicture = new JLabel("");
		panel.add(lblPicture, "cell 0 1");

		lblPicture_1 = new JLabel("");
		panel.add(lblPicture_1, "cell 1 1");

		lblPicture_2 = new JLabel("");
		panel.add(lblPicture_2, "cell 2 1");

		lblPicture_3 = new JLabel("");
		panel.add(lblPicture_3, "cell 3 1");

		lblPicture_4 = new JLabel("");
		panel.add(lblPicture_4, "cell 4 1");

		lblPictureDetail = new JLabel("");
		panel.add(lblPictureDetail, "cell 0 2");

		lblPicture_1Detail = new JLabel("");
		panel.add(lblPicture_1Detail, "cell 1 2");

		lblPicture_2Detail = new JLabel("");
		panel.add(lblPicture_2Detail, "cell 2 2");

		lblPicture_3Detail = new JLabel("");
		panel.add(lblPicture_3Detail, "cell 3 2");

		lblPicture_4Detail = new JLabel("");
		panel.add(lblPicture_4Detail, "cell 4 2");

		lblPicture_5 = new JLabel("");
		panel.add(lblPicture_5, "cell 0 3");

		lblPicture_6 = new JLabel("");
		panel.add(lblPicture_6, "cell 1 3");

		lblPicture_7 = new JLabel("");
		panel.add(lblPicture_7, "cell 2 3");

		lblPicture_8 = new JLabel("");
		panel.add(lblPicture_8, "cell 3 3");

		lblPicture_9 = new JLabel("");
		panel.add(lblPicture_9, "cell 4 3");

		lblPicture_5Detail = new JLabel("");
		panel.add(lblPicture_5Detail, "cell 0 4");

		lblPicture_6Detail = new JLabel("");
		panel.add(lblPicture_6Detail, "cell 1 4");

		lblPicture_7Detail = new JLabel("");
		panel.add(lblPicture_7Detail, "cell 2 4");

		lblPicture_8Detail = new JLabel("");
		panel.add(lblPicture_8Detail, "cell 3 4");

		lblPicture_9Detail = new JLabel("");
		panel.add(lblPicture_9Detail, "cell 4 4");

		JPanel panel_1 = new JPanel();
		panel_1.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		getContentPane().add(panel_1, "cell 0 4 3 1,grow");
		panel_1.setLayout(new MigLayout("", "[][grow]", "[][grow]"));

		JLabel lblTweetsPerHour = new JLabel("Tweets per hour:");
		panel_1.add(lblTweetsPerHour, "cell 0 0,alignx trailing");

		tfTweetsPerHour = new JTextField();
		tfTweetsPerHour.setEditable(false);
		panel_1.add(tfTweetsPerHour, "cell 1 0,alignx left");
		tfTweetsPerHour.setColumns(10);

		JLabel lblNewLabel_10 = new JLabel("Faces per hour:");
		panel_1.add(lblNewLabel_10, "cell 0 1");

		JPanel panel_2 = new JPanel();
		panel_1.add(panel_2, "cell 1 1,grow");
		panel_2.setLayout(new MigLayout("", "[][grow]", "[30.00,grow][33.00,grow]"));

		JLabel lblMale = new JLabel("Male:");
		panel_2.add(lblMale, "cell 0 0,alignx trailing");

		tfMale = new JTextField();
		tfMale.setEditable(false);
		panel_2.add(tfMale, "cell 1 0,alignx left");
		tfMale.setColumns(10);

		JLabel lblFemale = new JLabel("Female:");
		panel_2.add(lblFemale, "cell 0 1,alignx trailing");

		tfFemale = new JTextField();
		tfFemale.setEditable(false);
		panel_2.add(tfFemale, "cell 1 1,alignx left");
		tfFemale.setColumns(10);
	}

	public static void main(String[] args) {

		Gui gui = new Gui();
		gui.setLocationRelativeTo(null);
		gui.setVisible(true);

		cb = new ConfigurationBuilder();
		cb.setDebugEnabled(true).setOAuthConsumerKey("Ns3yS8PedorxabvYJnDZtap65")
				.setOAuthConsumerSecret("SKXL9rgvw7DilryTzElAe34p9yde3W3z68GAlm8VIL0GVt4Zg9")
				.setOAuthAccessToken("932921475656544256-bCPyFznWlffhl62aJolVexqJ5bXOKAz")
				.setOAuthAccessTokenSecret("rxwO40SWEk3GcPOI232F9dQlnp157BKBNanwN0x5mK7pF");

		twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

		listener = new StatusListener() {

			@Override
			public void onException(Exception arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onTrackLimitationNotice(int arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStatus(Status status) {

				if (i == 10)
					i = 0;

				System.out.println(status.getMediaEntities()[0].getExpandedURL());

				byte[] imgBytes = loadBytes(status.getMediaEntities()[0].getMediaURLHttps());
				saveBytes("tempImage.jpg", imgBytes);
				imgUrl = "tempImage.jpg";

				WeightedStandardPixelTrainer weightedStandardPixelTrainer = new WeightedStandardPixelTrainer();
				Mat[] faces = new FaceDetector().snipFace(imgUrl, new Size(90, 90));

				if (faces.length != 0) {
					int female = 0;
					int male = 0;

					weightedStandardPixelTrainer.load("src/res/knowledge/Knowledge.log");

					int faceNo = 1;
					for (Mat face : faces) {

						int prediction = weightedStandardPixelTrainer.predict(face);

						if (prediction == -1) {
							System.out.println("I think " + faceNo + " is not a face.");
						} else if (prediction == 0) {
							System.out.println("I think " + faceNo + " is a female.");
							female++;
							allFemale++;
						} else {
							System.out.println("I think " + faceNo + " is a male.");
							male++;
							allMale++;
						}

						faceNo++;
					}

					BufferedImage bimg = null;
					imgBytes = loadBytes(status.getMediaEntities()[0].getMediaURLHttps());
					saveBytes("tempImage" + i + ".jpg", imgBytes);
					imgUrl = "tempImage" + i + ".jpg";

					bimg = null;
					try {
						ImageResizer.resize(imgUrl, imgUrl, 100, 100);
						bimg = ImageIO.read(new File(imgUrl));
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					switch (i) {
					case 0:
						lblPicture.setIcon(new ImageIcon(bimg));
						lblPictureDetail.setText("Male:" + male + "Female: " + female);
						break;
					case 1:
						lblPicture_1.setIcon(new ImageIcon(bimg));
						lblPicture_1Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 2:
						lblPicture_2.setIcon(new ImageIcon(bimg));
						lblPicture_2Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 3:
						lblPicture_3.setIcon(new ImageIcon(bimg));
						lblPicture_3Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 4:
						lblPicture_4.setIcon(new ImageIcon(bimg));
						lblPicture_4Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 5:
						lblPicture_5.setIcon(new ImageIcon(bimg));
						lblPicture_5Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 6:
						lblPicture_6.setIcon(new ImageIcon(bimg));
						lblPicture_6Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 7:
						lblPicture_7.setIcon(new ImageIcon(bimg));
						lblPicture_7Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 8:
						lblPicture_8.setIcon(new ImageIcon(bimg));
						lblPicture_8Detail.setText("Male:" + male + "Female: " + female);
						break;
					case 9:
						lblPicture_9.setIcon(new ImageIcon(bimg));
						lblPicture_9Detail.setText("Male:" + male + "Female: " + female);
						break;
					default:
						break;
					}

					countTweets++;
					if (hours != 0) {
						tfTweetsPerHour.setText(Integer.toString(countTweets / hours));
						tfMale.setText(Integer.toString(allMale / hours));
						tfFemale.setText(Integer.toString(allFemale / hours));
					} else {
						tfTweetsPerHour.setText(Integer.toString(countTweets));
						tfMale.setText(Integer.toString(allMale));
						tfFemale.setText(Integer.toString(allFemale));
					}
					System.out.println(countTweets);

					i++;
				}
			}

			@Override
			public void onDeletionNotice(StatusDeletionNotice arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub

			}

			@Override
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub

			}

		};

		fq = new FilterQuery();
		fq.track(keyword);

		twitterStream.addListener(listener);
		twitterStream.filter(fq);
	}

	protected static byte[] loadBytes(String Imgurl) {
		URL url = null;
		try {
			url = new URL(Imgurl);
		} catch (MalformedURLException e1) {
			e1.printStackTrace();
		}
		InputStream is = null;
		byte[] imageBytes = null;
		try {
			is = url.openStream();
			imageBytes = IOUtils.toByteArray(is);
			return imageBytes;
		} catch (IOException e) {
			System.err.printf("Failed while reading bytes from %s: %s", url.toExternalForm(), e.getMessage());
			e.printStackTrace();
			// Perform any other exception handling that's appropriate.
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();

			}
		}
		return imageBytes;
	}

	protected static void saveBytes(String string, byte[] imgBytes) {
		InputStream input = new ByteArrayInputStream(imgBytes);
		OutputStream output;
		try {
			output = new FileOutputStream(string);
			IOUtils.copy(input, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	protected void filterTwitterStream() {
		keyword = tfListenFor.getText();

		if (twitterStream == null) {
			twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
			twitterStream.addListener(listener);
		}

		FilterQuery qry = new FilterQuery();

		qry.track(keyword);

		twitterStream.filter(qry);
	}
}

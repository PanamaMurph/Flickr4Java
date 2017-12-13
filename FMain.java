import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.flickr4java.flickr.Flickr;
import com.flickr4java.flickr.FlickrException;
import com.flickr4java.flickr.REST;
import com.flickr4java.flickr.photos.Exif;
import com.flickr4java.flickr.photos.GeoData;
import com.flickr4java.flickr.photos.Photo;
import com.flickr4java.flickr.photos.PhotoList;
import com.flickr4java.flickr.photos.PhotosInterface;
import com.flickr4java.flickr.photos.SearchParameters;
import com.flickr4java.flickr.photos.comments.Comment;
import com.flickr4java.flickr.photos.comments.CommentsInterface;
import com.flickr4java.flickr.photos.geo.GeoInterface;
import com.flickr4java.flickr.photos.suggestions.Suggestion;
import com.flickr4java.flickr.photos.suggestions.SuggestionList;
import com.flickr4java.flickr.photos.suggestions.SuggestionsInterface;
import com.flickr4java.flickr.stats.StatsInterface;

public class FMain {

	public static void main(String[] args)
			throws FlickrException, IOException, ImageProcessingException, InterruptedException {
		String apiKey = "fe6e7ff80fcb9dd992845ef9d88ef56b";
		String secret = "47cc097a1d6578b3";
		String server = "www.flickr.com";

		while (true) {

			File fileFlickr = new File("C:\\Data\\Flickr\\imageTags.txt");

			// if file doesn't exists, then create it
			if (!fileFlickr.exists()) {
				fileFlickr.createNewFile();
			}

			FileWriter fw = new FileWriter(fileFlickr.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);

			// Create a Flickr instance with your data. No need to authenticate
			Flickr flickr = new Flickr(apiKey, secret, new REST());

			SearchParameters searchParameters = new SearchParameters();
			
			searchParameters.setWoeId("2378319");
			//Where on earth id set for Charleston
			
			
			System.out.print("setting parameters \n");
			
			
			PhotoList<Photo> list = flickr.getPhotosInterface().search(searchParameters, 100, 100);

			System.out.println(list.size());

			File flickrDirectory = new File("C:\\Data\\Flickr");
			// File flickrFile;

			PhotosInterface pi = new PhotosInterface(apiKey, secret, new REST());

			CommentsInterface ci = new CommentsInterface(apiKey, secret, new REST());

			SuggestionsInterface si = new SuggestionsInterface(apiKey, secret, new REST());

			StatsInterface sa = new StatsInterface(apiKey, secret, new REST());

			for (int i = 0; i < list.size(); i++) {
				Photo photo  = list.get(i);

				String id = photo.getId();
				String url = list.get(i).getUrl();

				/*bw.write("EXIF------------");

				try {
					java.util.Collection<Exif> exifs = pi.getExif(id, secret);

					for (Exif e : exifs) {
						bw.write(e.getTag() + " = " + e.getRaw() + "\n");
						bw.write("\n");
					}

					 bw.write("\n");
					 System.out.println("HERE>>>>" +
					 pi.getExif(id,secret).toString());
				} catch (Exception ex) {
					System.out.println("Exif Information not accessible!!");
				}

				bw.write("COMMENTS------------");
				try {
					List<Comment> lcomment = ci.getList(id);
					System.out.println("lcomment SIZE = " + lcomment.size());

					for (Comment c : lcomment) {
						bw.write("COMMENTS>>>> " + 
					c.getAuthor() + " , " + 
					c.getDateCreate() + " , " + c.getText());
						bw.write("\n");
					}
				} catch (Exception ex) {
					System.out.println("Comments not found");
				}
				bw.write("\n");

				bw.write("Suggestions------------");
				try {
					SuggestionList<Suggestion> sg = si.getList(id);
					System.out.println("Suggestion SIZE = " + sg.size());

					for (Suggestion s : sg) {
						bw.write("SUGGEST>>>> " + s.getNote() + " , " + s.getLocation());
						bw.write("\n");
					}
				} catch (Exception ex) {
					System.out.println("Suggestions not found");
				}*/
				bw.write("\n");

				float latitude = 0;
				float longitude = 0;

				GeoInterface gi = pi.getGeoInterface();
				if (gi != null) {
					try {
						GeoData ga = gi.getLocation(id);
						if (ga != null) {
							bw.write("HERE GEO >>>>" + ga.toString());
							latitude = ga.getLatitude();
							longitude = ga.getLongitude();
						}
					} catch (Exception ex) {
						System.out.println(" No Location Information!! ");
					}
				} else {
					System.out.println(" No Location Information!! ");
				}

				BufferedImage image = photo.getLargeImage();
				File outputfile = new File(
						flickrDirectory.getPath() + File.separator + photo.getId() + "." + photo.getOriginalFormat());
				ImageIO.write(image, photo.getOriginalFormat(), outputfile);

				Photo p1 = pi.getInfo(id, secret);

				// System.out.println(outputfile.getName());
				// Metadata metadata =
				// ImageMetadataReader.readMetadata(outputfile);
				// printImageTags(1, metadata);
				// System.out.println(outputfile.getName());

				/*String place = p1.getPlaceId();*/
				String desc = p1.getDescription();
				Date added = p1.getDateAdded();
				Date posted = p1.getDatePosted();
				Date taken = p1.getDateTaken();
				/*String farm = p1.getFarm();*/
				String media = p1.getMedia();
				String owner = p1.getOwner().getRealName();
				String photoTags = p1.getTags().toString();
				int noCommnets = p1.getComments();
				int stats = 0;
				if (p1.getStats() != null) {
					stats = p1.getStats().getFavorites();
				}
				/*
				 * String locality = "No Locality"; if(p1.getLocality() !=
				 * null){ locality = p1.getLocality().toString(); }
				 */

				bw.write(" url: " + url + "\n" + /*"\t" + " place: " + place + "\t" + " added: " + added + "\t" +*/ " posted: "
						+ posted + "\t" + " taken: " + taken + /*"\t" + " farm: " + farm +*/ "\t" + " latitude: " + latitude
						+ "\t" + " longitude: " + longitude + "\n" + " id: " + id + "\t" + " media: " + media + "\t"
						+ " owner: " + owner + "\t" + " photoTags: " + photoTags + "\n" + " desc: " + desc + "\n"
						+ " no of comments: " + noCommnets + "\t" /*+ " no of views: " + noViews + "\t"*/ + " stats - fav: "
						+ stats +
						/* "\t" + " locality: " + locality + */ "\n");

			} 
			long fifteenMin = 20 * 60 * 1000;
			Thread.sleep(fifteenMin);
			System.out.println("Going back in the loop!! ");
		} // End while

	}

	private static void printImageTags(int approachCount, Metadata metadata) {
		System.out.println();
		System.out.println("*** APPROACH " + approachCount + " ***");
		System.out.println();

		// iterate over the exit data and print
		for (Directory directory : metadata.getDirectories()) {
			for (com.drew.metadata.Tag tag : directory.getTags())
				System.out.println(tag);
			for (String error : directory.getErrors())
				System.err.println("ERROR: " + error);
		}
	}

}

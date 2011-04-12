package edu.mills.cs280.audiorunner;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.mills.cs280.audiorunner.Jlayer.Bitstream;
import edu.mills.cs280.audiorunner.Jlayer.BitstreamException;
import edu.mills.cs280.audiorunner.Jlayer.Decoder;
import edu.mills.cs280.audiorunner.Jlayer.DecoderException;
import edu.mills.cs280.audiorunner.Jlayer.Header;

public class AudioAnalyzer extends Thread{

	private File file;
	private InputStream inputStream;
	private Bitstream bitstream;
	private Decoder decoder;

	public AudioAnalyzer(String fileLocation){
		file = new File(fileLocation);
		try {
			inputStream = new BufferedInputStream(new FileInputStream(file));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		bitstream = new Bitstream(inputStream);
		decoder = new Decoder();
	}

	public List<short[]> getData()
	{						
		List<short[]> data = new ArrayList<short[]>();

		try {

			Header header;
			for(int i = 0; i < 200;i++){
				header = bitstream.readFrame();
				SampleBuffer output = (SampleBuffer)decoder.decodeFrame(header, bitstream);
				data.add(output.getBuffer());
			}


		} catch (BitstreamException e) {
			e.printStackTrace();
		} catch (DecoderException e) {
			e.printStackTrace();
		}

		return data;

	}

}

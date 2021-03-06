package gui;

import gui.model.TracksTableModel;

import java.awt.*;
import java.io.IOException;

import javax.swing.*;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import model.Track;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * The main window
 */
public class BaseView {
	
	private static String WINDOW_TITLE = "STAR Broadcaster";
	private JFrame frame;
	private int numChannels = 4;
	private List<ChannelWidget> channelWidgets = new ArrayList<ChannelWidget>();
	
	// Table model for tracks visible in browser
	private TracksTableModel tracksTableModel;
	
	// All tracks
	private List<Track> tracks;
	
	/**
	 * Setup the window and display it
	 */
	public BaseView() {
		
		// Initialise window and maximise it
		if(isMacOSX()) System.setProperty("com.apple.mrj.application.apple.menu.about.name", WINDOW_TITLE);
		frame = new JFrame(WINDOW_TITLE);
		frame.setExtendedState(frame.getExtendedState()|JFrame.MAXIMIZED_BOTH);
		int minWidth = numChannels * 50 * 3;
		int minHeight = 600;
		frame.setMinimumSize(new Dimension(minWidth, minHeight));
		
		setupMenuBar();
		
		Container contentPane = frame.getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		setupBrowser(contentPane);
		setupPlayoutChannels(contentPane);
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		// TODO tracksTableModel.addTrack();
	}
	
	/**
	 * Setup the main menu bar along the top of the window
	 */
	private void setupMenuBar() {
		MenuBar menuBar = new MenuBar();
		menuBar.setName(WINDOW_TITLE);
		Menu file = new Menu("File");
		Menu view = new Menu("View");
		Menu help = new Menu("Help");
		menuBar.add(file);
		menuBar.add(view);
		menuBar.add(help);
		frame.setMenuBar(menuBar);
	}
	
	private void setupBrowser(Container container) {
		
		JPanel browser = new JPanel();
		container.add(browser, BorderLayout.NORTH);
		
		tracksTableModel = new TracksTableModel();
	    JTable table = new JTable(tracksTableModel);
	    table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 
	    table.setDragEnabled(true);
	    table.setTransferHandler(new TableTrackTransferHandler(table));
		
		JScrollPane listScroller = new JScrollPane(table);
		//listScroller.setPreferredSize(new Dimension(250, 80));
		browser.add(listScroller);
		
	}
	
	public void updateTracks(List<Track> tracks) {
		this.tracks = tracks;
		tracksTableModel.updateTracks(tracks); // TODO: this table shouldn't show everything if we do a search util
	}
	
	/**
	 * Setup all of the playout channels, adding them to the container
	 * @param container Parent container
	 */
	private void setupPlayoutChannels(Container container) {
		
		// Add channels to the bottom of the window
		Container channels = new Container();
		container.add(channels, BorderLayout.SOUTH);
		
		LayoutManager layoutManager = new BoxLayout(channels, BoxLayout.X_AXIS);
		channels.setLayout(layoutManager);
		
		for(int i = 0; i < numChannels; i++) {
			setupPlayoutChannel(i, channels);
		}
	}
	
	/**
	 * Setup a playout channel and add it to the container
	 * @param channelNum Channel number
	 * @param container Parent container
	 */
	private void setupPlayoutChannel(int channelNum, Container container) {
		JPanel channelPanel = new JPanel();
		ChannelWidget channelWidget = new ChannelWidget(channelNum, channelPanel);
		channelWidgets.add(channelWidget);
		container.add(channelPanel);
	}
	
    private static boolean isMacOSX() {
        return System.getProperty("os.name").indexOf("Mac OS X") >= 0;
    }
}

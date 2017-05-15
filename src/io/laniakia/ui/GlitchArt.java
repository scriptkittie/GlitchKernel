package io.laniakia.ui;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

import javax.swing.DefaultComboBoxModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import io.laniakia.algo.GlitchAlgorithm;
import io.laniakia.util.GlitchTypes;
import io.laniakia.util.SelectionTypes;

public class GlitchArt extends JFrame {

	public JPanel contentPane;
	public JButton btnOpen;
	public JTextField txtPath;
	public JButton btnExport;
	public JPanel imagePreviewPanel;
	public JLabel lblImageRender;
	public JLayeredPane layeredPane;
	public JLabel label_1;
	public JButton button_2;
	public JComboBox jAlgorithmSelection;
	public JButton button_3;
	public JButton button_4;
	public JList<GlitchAlgorithm> jAlgorithmList;
	public JButton button_5;
	public JButton button_6;
	public JPanel pixelSortPanel;
	public JLabel lblDistortion;
	public JSlider pixelSortBrightness;
	public JLabel lblDistortionLength;
	public JSlider pixelSortLength;
	public JPanel fractalSortPanel;
	public JLabel lblDistortionAmount;
	public JSlider fractalSortDistortionAmount;
	public JPanel pixelSlicePanel;
	public JLabel lblDistortionAmount_1;
	public JSlider pixelSliceAmount;
	public JPanel randomPixelPanel;
	public JLabel lblRandomization;
	public JSlider randomRandomization;
	public JLabel lblNewLabel;
	public JSlider randomDistortionAmount;
	public JPanel dataAsSoundPanel;
	public JLabel lblBitrateDistort;
	public JSlider dataAsSoundBitRate;
	public JPanel rgbShiftPanel;
	public JLabel lblEffect;
	public JComboBox rgbShiftColor;
	public JLabel lblShiftAmount;
	public JSlider rgbShiftAmount;
	public JPanel brightnessPanel;
	public JLabel lblBrightness;
	public JSlider brightnessSlider;
	public JPanel offsetSortPanel;
	public JLabel lblDistortionAmount_2;
	public JSlider offsetDistortionAmount;
	public JLabel lblDistortionBrightness;
	public JSlider offsetBrightness;
	public JLabel lblScanlines;
	public JComboBox offsetScanLines;
	public JPanel verticalPixelSortPanel;
	public JLabel lblInterval;
	public JSlider verticalInterval;
	public JButton btnClearSelection;
	public JPanel randomPixelGlitchPanel;
	public JLabel lblDistortionAmount_3;
	public JSlider randomGlitchDistortion;
	public JPanel bitSortPanel;
	public JLabel lblNewLabel_1;
	public JLabel lblNewLabel_3;
	public JLabel lblVerticalIncrement;
	public JSlider bitSortDistortion;
	public JSlider bitSortVerticalInterval;
	public JSlider bitSortHorizontalInterval;
	
	public GlitchUIManager glitchUIManager;
	public SelectionManager selectionPointManager;
	public int resizedHeight;
	public int resizedWidth;
	public int realHeight;
	public int realWidth;
	public JButton btnReset;
	public JComboBox selectionType;
	private JLabel lblSelectionType;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
					GlitchArt frame = new GlitchArt();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public GlitchArt() {
		setResizable(false);
		setTitle("Glitch Kernel");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 957, 482);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		glitchUIManager = new GlitchUIManager(this);
		selectionPointManager = new SelectionManager(glitchUIManager.getGlitchController(), this);
		
		btnOpen = new JButton("Open");
		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.glitchUIManager.openFile();
			}
		});
		
		txtPath = new JTextField();
		txtPath.setEditable(false);
		txtPath.setColumns(10);
		
		btnExport = new JButton("Export");
		btnExport.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e)
			{
				GlitchArt.this.glitchUIManager.exportFile();
			}
		});
		
		imagePreviewPanel = new JPanel();
		
		lblImageRender = new JLabel("") 
		{
			public void paintComponent(Graphics g) 
			{
				super.paintComponent(g);
				GlitchArt.this.selectionPointManager.processImageRenderPaint(g);
			}
		};
		lblImageRender.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseReleased(MouseEvent e) 
			{
				GlitchArt.this.selectionPointManager.processMouseRelease(e);
			}
			
			@Override
			public void mousePressed(MouseEvent e) 
			{
				GlitchArt.this.selectionPointManager.processMousePress(e);
			}
		});
		lblImageRender.addMouseMotionListener(new MouseMotionAdapter() 
		{
			@Override
			public void mouseDragged(MouseEvent e) 
			{
				GlitchArt.this.selectionPointManager.processMouseDrag(e);
			}
		});
		lblImageRender.setBackground(Color.ORANGE);
		GroupLayout gl_imagePreviewPanel = new GroupLayout(imagePreviewPanel);
		gl_imagePreviewPanel.setHorizontalGroup(
			gl_imagePreviewPanel.createParallelGroup(Alignment.LEADING)
				.addGap(0, 511, Short.MAX_VALUE)
				.addComponent(lblImageRender, GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
		);
		gl_imagePreviewPanel.setVerticalGroup(
			gl_imagePreviewPanel.createParallelGroup(Alignment.TRAILING)
				.addGap(0, 390, Short.MAX_VALUE)
				.addComponent(lblImageRender, Alignment.LEADING, GroupLayout.DEFAULT_SIZE, 390, Short.MAX_VALUE)
		);
		imagePreviewPanel.setLayout(gl_imagePreviewPanel);
		
		layeredPane = new JLayeredPane();
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(21)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnOpen, GroupLayout.PREFERRED_SIZE, 126, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(txtPath, GroupLayout.DEFAULT_SIZE, 629, Short.MAX_VALUE)
							.addGap(18)
							.addComponent(btnExport, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 383, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
							.addComponent(imagePreviewPanel, GroupLayout.PREFERRED_SIZE, 511, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnOpen)
						.addComponent(txtPath, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnExport))
					.addGap(19)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(imagePreviewPanel, GroupLayout.PREFERRED_SIZE, 390, GroupLayout.PREFERRED_SIZE)
						.addComponent(layeredPane, GroupLayout.PREFERRED_SIZE, 1010, GroupLayout.PREFERRED_SIZE))
					.addContainerGap())
		);
		
		label_1 = new JLabel("Algorithm:");
		
		button_2 = new JButton("Regenerate");
		button_2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.glitchUIManager.refreshPreviewImage();
			}
		});
		
		jAlgorithmSelection = new JComboBox();
		jAlgorithmSelection.setModel(new DefaultComboBoxModel(GlitchTypes.values()));
		
		button_3 = new JButton("Add");
		button_3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.glitchUIManager.addAlgorithm();
			}
		});
		
		button_4 = new JButton("Remove");
		button_4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.glitchUIManager.removeAlgorithm();
			}
		});
		
		jAlgorithmList = new JList();
		jAlgorithmList.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) 
			{
				if (!e.getValueIsAdjusting()) 
				{
					GlitchArt.this.glitchUIManager.changeAlgorithmSelection();
					GlitchArt.this.selectionPointManager.drawSelectedRectangle();
				}
			}
		});
		jAlgorithmList.setFont(new Font("Tahoma", Font.PLAIN, 10));
		
		button_5 = new JButton("\u2191");
		button_5.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeAlgorithmOrder(true);
			}
		});
		
		button_6 = new JButton("\u2193");
		button_6.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeAlgorithmOrder(false);
			}
		});
		
		pixelSortPanel = new JPanel();
		pixelSortPanel.setVisible(false);
		
		fractalSortPanel = new JPanel();
		fractalSortPanel.setVisible(false);
		
		pixelSlicePanel = new JPanel();
		pixelSlicePanel.setVisible(false);
		
		randomPixelPanel = new JPanel();
		randomPixelPanel.setVisible(false);
		
		dataAsSoundPanel = new JPanel();
		dataAsSoundPanel.setVisible(false);
		
		rgbShiftPanel = new JPanel();
		rgbShiftPanel.setVisible(false);
		
		brightnessPanel = new JPanel();
		brightnessPanel.setVisible(false);
		
		offsetSortPanel = new JPanel();
		offsetSortPanel.setVisible(false);
		
		verticalPixelSortPanel = new JPanel();
		verticalPixelSortPanel.setVisible(false);
		
		btnClearSelection = new JButton("Clear Selection");
		btnClearSelection.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.selectionPointManager.clearSelectionPoint();
				GlitchArt.this.glitchUIManager.refreshPreviewImage();
			}
		});
		
		randomPixelGlitchPanel = new JPanel();
		randomPixelGlitchPanel.setVisible(false);
		GridBagLayout gbl_randomPixelGlitchPanel = new GridBagLayout();
		gbl_randomPixelGlitchPanel.columnWidths = new int[]{0, 0, 0};
		gbl_randomPixelGlitchPanel.rowHeights = new int[]{0, 0};
		gbl_randomPixelGlitchPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_randomPixelGlitchPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		randomPixelGlitchPanel.setLayout(gbl_randomPixelGlitchPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.VERTICAL_PIXEL_GLITCH, randomPixelGlitchPanel);
		
		lblDistortionAmount_3 = new JLabel("Distortion Amount:");
		GridBagConstraints gbc_lblDistortionAmount_3 = new GridBagConstraints();
		gbc_lblDistortionAmount_3.anchor = GridBagConstraints.WEST;
		gbc_lblDistortionAmount_3.insets = new Insets(0, 0, 0, 5);
		gbc_lblDistortionAmount_3.gridx = 0;
		gbc_lblDistortionAmount_3.gridy = 0;
		randomPixelGlitchPanel.add(lblDistortionAmount_3, gbc_lblDistortionAmount_3);
		
		randomGlitchDistortion = new JSlider();
		randomGlitchDistortion.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(randomGlitchDistortion);
			}
		});
		randomGlitchDistortion.setValue(1);
		randomGlitchDistortion.setMinimum(1);
		GridBagConstraints gbc_randomGlitchDistortion = new GridBagConstraints();
		gbc_randomGlitchDistortion.anchor = GridBagConstraints.WEST;
		gbc_randomGlitchDistortion.gridx = 1;
		gbc_randomGlitchDistortion.gridy = 0;
		randomPixelGlitchPanel.add(randomGlitchDistortion, gbc_randomGlitchDistortion);
		
		bitSortPanel = new JPanel();
		bitSortPanel.setVisible(false);
		
		
		
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.GENERIC_PIXEL_SORT_V1, pixelSortPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.PIXEL_SLICE, pixelSlicePanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.OFFSET_PIXEL_SORT, offsetSortPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.FRACTAL_PIXEL_DISPERSE, fractalSortPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.DATA_AS_SOUND, dataAsSoundPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.RGB_SHIFT_FILTER, rgbShiftPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.BRIGHTNESS_FILTER, brightnessPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.BIT_SORT, bitSortPanel);
		//this.glitchUIManager.getjPanelMapping().put(GlitchTypes.RANDOMIZED_PIXEL_SORT, randomPixelPanel);
		this.glitchUIManager.getjPanelMapping().put(GlitchTypes.HORIZONTAL_PIXEL_SORT, verticalPixelSortPanel);
		
		btnReset = new JButton("Reset");
		btnReset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) 
			{
				GlitchArt.this.glitchUIManager.resetUI();
			}
		});
		
		selectionType = new JComboBox();
		selectionType.setModel(new DefaultComboBoxModel(SelectionTypes.values()));
		
		lblSelectionType = new JLabel("Selection Type:");
		
		
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		gl_layeredPane.setHorizontalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addComponent(bitSortPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(verticalPixelSortPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(offsetSortPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(rgbShiftPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(dataAsSoundPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(randomPixelPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(pixelSlicePanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(pixelSortPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_layeredPane.createSequentialGroup()
									.addComponent(jAlgorithmList, GroupLayout.PREFERRED_SIZE, 232, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
										.addComponent(selectionType, 0, 103, Short.MAX_VALUE)
										.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING, false)
											.addComponent(lblSelectionType)
											.addComponent(button_6, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
											.addComponent(button_5, GroupLayout.PREFERRED_SIZE, 39, GroupLayout.PREFERRED_SIZE)
											.addComponent(btnReset, GroupLayout.DEFAULT_SIZE, 103, Short.MAX_VALUE))))
								.addGroup(gl_layeredPane.createSequentialGroup()
									.addGroup(gl_layeredPane.createParallelGroup(Alignment.TRAILING, false)
										.addGroup(gl_layeredPane.createSequentialGroup()
											.addComponent(label_1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
											.addPreferredGap(ComponentPlacement.RELATED)
											.addComponent(button_2, GroupLayout.PREFERRED_SIZE, 103, GroupLayout.PREFERRED_SIZE))
										.addComponent(jAlgorithmSelection, Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 175, GroupLayout.PREFERRED_SIZE))
									.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING, false)
										.addGroup(gl_layeredPane.createSequentialGroup()
											.addGap(6)
											.addComponent(button_3, GroupLayout.PREFERRED_SIZE, 51, GroupLayout.PREFERRED_SIZE)
											.addGap(6)
											.addComponent(button_4, GroupLayout.PREFERRED_SIZE, 71, GroupLayout.PREFERRED_SIZE))
										.addGroup(gl_layeredPane.createSequentialGroup()
											.addPreferredGap(ComponentPlacement.UNRELATED)
											.addComponent(btnClearSelection, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
							.addContainerGap(20, Short.MAX_VALUE))
						.addComponent(fractalSortPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(brightnessPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)
						.addComponent(randomPixelGlitchPanel, GroupLayout.DEFAULT_SIZE, 373, Short.MAX_VALUE)))
		);
		gl_layeredPane.setVerticalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addGap(15)
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(label_1)
						.addComponent(button_2)
						.addComponent(btnClearSelection))
					.addGap(22)
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(1)
							.addComponent(jAlgorithmSelection, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addComponent(button_3)
						.addComponent(button_4))
					.addGap(18)
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(jAlgorithmList, GroupLayout.PREFERRED_SIZE, 147, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addComponent(button_5)
							.addGap(10)
							.addComponent(button_6)
							.addGap(11)
							.addComponent(btnReset)
							.addGap(8)
							.addComponent(lblSelectionType)
							.addGap(1)
							.addComponent(selectionType, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(pixelSortPanel, GroupLayout.PREFERRED_SIZE, 66, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(fractalSortPanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(pixelSlicePanel, GroupLayout.PREFERRED_SIZE, 42, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(randomPixelPanel, GroupLayout.PREFERRED_SIZE, 62, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(dataAsSoundPanel, GroupLayout.PREFERRED_SIZE, 41, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(rgbShiftPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(brightnessPanel, GroupLayout.PREFERRED_SIZE, 38, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(offsetSortPanel, GroupLayout.PREFERRED_SIZE, 85, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.RELATED)
					.addComponent(verticalPixelSortPanel, GroupLayout.PREFERRED_SIZE, 37, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(randomPixelGlitchPanel, GroupLayout.PREFERRED_SIZE, 35, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(bitSortPanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
					.addContainerGap(46, Short.MAX_VALUE))
		);
		GridBagLayout gbl_bitSortPanel = new GridBagLayout();
		gbl_bitSortPanel.columnWidths = new int[]{0, 0, 0};
		gbl_bitSortPanel.rowHeights = new int[]{0, 0, 0, 0, 0};
		gbl_bitSortPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_bitSortPanel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		bitSortPanel.setLayout(gbl_bitSortPanel);
		
		lblNewLabel_1 = new JLabel("Distortion:");
		GridBagConstraints gbc_lblNewLabel_1 = new GridBagConstraints();
		gbc_lblNewLabel_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_1.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_1.gridx = 0;
		gbc_lblNewLabel_1.gridy = 0;
		bitSortPanel.add(lblNewLabel_1, gbc_lblNewLabel_1);
		
		bitSortDistortion = new JSlider();
		bitSortDistortion.setMinorTickSpacing(5);
		bitSortDistortion.setInverted(true);
		bitSortDistortion.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(bitSortDistortion);
			}
		});
		bitSortDistortion.setValue(300);
		bitSortDistortion.setMaximum(600);
		GridBagConstraints gbc_bitSortThreshHold = new GridBagConstraints();
		gbc_bitSortThreshHold.anchor = GridBagConstraints.WEST;
		gbc_bitSortThreshHold.insets = new Insets(0, 0, 5, 0);
		gbc_bitSortThreshHold.gridx = 1;
		gbc_bitSortThreshHold.gridy = 0;
		bitSortPanel.add(bitSortDistortion, gbc_bitSortThreshHold);
		
		lblNewLabel_3 = new JLabel("Horizontal Increment:");
		GridBagConstraints gbc_lblNewLabel_3 = new GridBagConstraints();
		gbc_lblNewLabel_3.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel_3.insets = new Insets(0, 0, 5, 5);
		gbc_lblNewLabel_3.gridx = 0;
		gbc_lblNewLabel_3.gridy = 1;
		bitSortPanel.add(lblNewLabel_3, gbc_lblNewLabel_3);
		
		bitSortVerticalInterval = new JSlider();
		bitSortVerticalInterval.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(bitSortVerticalInterval);
			}
		});
		bitSortVerticalInterval.setValue(0);
		bitSortVerticalInterval.setMinimum(-100);
		GridBagConstraints gbc_bitSortVerticalIncrement = new GridBagConstraints();
		gbc_bitSortVerticalIncrement.insets = new Insets(0, 0, 5, 0);
		gbc_bitSortVerticalIncrement.gridx = 1;
		gbc_bitSortVerticalIncrement.gridy = 1;
		bitSortPanel.add(bitSortVerticalInterval, gbc_bitSortVerticalIncrement);
		
		lblVerticalIncrement = new JLabel("Vertical Increment:");
		GridBagConstraints gbc_lblVerticalIncrement = new GridBagConstraints();
		gbc_lblVerticalIncrement.insets = new Insets(0, 0, 5, 5);
		gbc_lblVerticalIncrement.anchor = GridBagConstraints.WEST;
		gbc_lblVerticalIncrement.gridx = 0;
		gbc_lblVerticalIncrement.gridy = 2;
		bitSortPanel.add(lblVerticalIncrement, gbc_lblVerticalIncrement);
		
		bitSortHorizontalInterval = new JSlider();
		bitSortHorizontalInterval.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(bitSortHorizontalInterval);
			}
		});
		bitSortHorizontalInterval.setValue(0);
		bitSortHorizontalInterval.setMinimum(-100);
		GridBagConstraints gbc_bitSortHorizontalIncrement = new GridBagConstraints();
		gbc_bitSortHorizontalIncrement.insets = new Insets(0, 0, 5, 0);
		gbc_bitSortHorizontalIncrement.gridx = 1;
		gbc_bitSortHorizontalIncrement.gridy = 2;
		bitSortPanel.add(bitSortHorizontalInterval, gbc_bitSortHorizontalIncrement);
		GridBagLayout gbl_verticalPixelSortPanel = new GridBagLayout();
		gbl_verticalPixelSortPanel.columnWidths = new int[]{0, 0, 0};
		gbl_verticalPixelSortPanel.rowHeights = new int[]{0, 0};
		gbl_verticalPixelSortPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_verticalPixelSortPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		verticalPixelSortPanel.setLayout(gbl_verticalPixelSortPanel);
		
		lblInterval = new JLabel("Interval:");
		GridBagConstraints gbc_lblInterval = new GridBagConstraints();
		gbc_lblInterval.insets = new Insets(0, 0, 0, 5);
		gbc_lblInterval.gridx = 0;
		gbc_lblInterval.gridy = 0;
		verticalPixelSortPanel.add(lblInterval, gbc_lblInterval);
		
		verticalInterval = new JSlider();
		verticalInterval.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e)
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(verticalInterval);
			}
		});
		verticalInterval.setValue(75);
		verticalInterval.setMaximum(150);
		verticalInterval.setMinimum(1);
		GridBagConstraints gbc_verticalInterval = new GridBagConstraints();
		gbc_verticalInterval.gridx = 1;
		gbc_verticalInterval.gridy = 0;
		verticalPixelSortPanel.add(verticalInterval, gbc_verticalInterval);
		GridBagLayout gbl_offsetSortPanel = new GridBagLayout();
		gbl_offsetSortPanel.columnWidths = new int[]{0, 0, 0};
		gbl_offsetSortPanel.rowHeights = new int[]{0, 0, 0, 0};
		gbl_offsetSortPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_offsetSortPanel.rowWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		offsetSortPanel.setLayout(gbl_offsetSortPanel);
		
		lblDistortionAmount_2 = new JLabel("Distortion Amount:");
		GridBagConstraints gbc_lblDistortionAmount_2 = new GridBagConstraints();
		gbc_lblDistortionAmount_2.anchor = GridBagConstraints.WEST;
		gbc_lblDistortionAmount_2.insets = new Insets(0, 0, 5, 5);
		gbc_lblDistortionAmount_2.gridx = 0;
		gbc_lblDistortionAmount_2.gridy = 0;
		offsetSortPanel.add(lblDistortionAmount_2, gbc_lblDistortionAmount_2);
		
		offsetDistortionAmount = new JSlider();
		offsetDistortionAmount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(offsetDistortionAmount);
			}
		});
		offsetDistortionAmount.setValue(6);
		offsetDistortionAmount.setMaximum(10);
		offsetDistortionAmount.setMinimum(1);
		GridBagConstraints gbc_offsetDistortionAmount = new GridBagConstraints();
		gbc_offsetDistortionAmount.anchor = GridBagConstraints.WEST;
		gbc_offsetDistortionAmount.insets = new Insets(0, 0, 5, 0);
		gbc_offsetDistortionAmount.gridx = 1;
		gbc_offsetDistortionAmount.gridy = 0;
		offsetSortPanel.add(offsetDistortionAmount, gbc_offsetDistortionAmount);
		
		lblDistortionBrightness = new JLabel("Distortion Brightness:");
		GridBagConstraints gbc_lblDistortionBrightness = new GridBagConstraints();
		gbc_lblDistortionBrightness.anchor = GridBagConstraints.WEST;
		gbc_lblDistortionBrightness.insets = new Insets(0, 0, 5, 5);
		gbc_lblDistortionBrightness.gridx = 0;
		gbc_lblDistortionBrightness.gridy = 1;
		offsetSortPanel.add(lblDistortionBrightness, gbc_lblDistortionBrightness);
		
		offsetBrightness = new JSlider();
		offsetBrightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(offsetBrightness);
			}
		});
		offsetBrightness.setValue(5);
		offsetBrightness.setMaximum(10);
		offsetBrightness.setMinimum(1);
		GridBagConstraints gbc_offsetBrightness = new GridBagConstraints();
		gbc_offsetBrightness.anchor = GridBagConstraints.WEST;
		gbc_offsetBrightness.insets = new Insets(0, 0, 5, 0);
		gbc_offsetBrightness.gridx = 1;
		gbc_offsetBrightness.gridy = 1;
		offsetSortPanel.add(offsetBrightness, gbc_offsetBrightness);
		
		lblScanlines = new JLabel("Scanlines:");
		GridBagConstraints gbc_lblScanlines = new GridBagConstraints();
		gbc_lblScanlines.anchor = GridBagConstraints.WEST;
		gbc_lblScanlines.insets = new Insets(0, 0, 0, 5);
		gbc_lblScanlines.gridx = 0;
		gbc_lblScanlines.gridy = 2;
		offsetSortPanel.add(lblScanlines, gbc_lblScanlines);
		
		offsetScanLines = new JComboBox();
		offsetScanLines.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(offsetScanLines);
			}
		});
		offsetScanLines.setModel(new DefaultComboBoxModel(new String[] {"NO", "YES"}));
		GridBagConstraints gbc_offsetScanLines = new GridBagConstraints();
		gbc_offsetScanLines.anchor = GridBagConstraints.WEST;
		gbc_offsetScanLines.gridx = 1;
		gbc_offsetScanLines.gridy = 2;
		offsetSortPanel.add(offsetScanLines, gbc_offsetScanLines);
		GridBagLayout gbl_brightnessPanel = new GridBagLayout();
		gbl_brightnessPanel.columnWidths = new int[]{0, 0, 0};
		gbl_brightnessPanel.rowHeights = new int[]{0, 0};
		gbl_brightnessPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_brightnessPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		brightnessPanel.setLayout(gbl_brightnessPanel);
		
		lblBrightness = new JLabel("Brightness:");
		GridBagConstraints gbc_lblBrightness = new GridBagConstraints();
		gbc_lblBrightness.anchor = GridBagConstraints.WEST;
		gbc_lblBrightness.insets = new Insets(0, 0, 0, 5);
		gbc_lblBrightness.gridx = 0;
		gbc_lblBrightness.gridy = 0;
		brightnessPanel.add(lblBrightness, gbc_lblBrightness);
		
		brightnessSlider = new JSlider();
		brightnessSlider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(brightnessSlider);
			}
		});
		brightnessSlider.setMaximum(10);
		brightnessSlider.setValue(0);
		brightnessSlider.setMinimum(-10);
		GridBagConstraints gbc_brightnessSlider = new GridBagConstraints();
		gbc_brightnessSlider.gridx = 1;
		gbc_brightnessSlider.gridy = 0;
		brightnessPanel.add(brightnessSlider, gbc_brightnessSlider);
		GridBagLayout gbl_rgbShiftPanel = new GridBagLayout();
		gbl_rgbShiftPanel.columnWidths = new int[]{0, 0, 0};
		gbl_rgbShiftPanel.rowHeights = new int[]{0, 0, 0};
		gbl_rgbShiftPanel.columnWeights = new double[]{0.0, 1.0, Double.MIN_VALUE};
		gbl_rgbShiftPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		rgbShiftPanel.setLayout(gbl_rgbShiftPanel);
		
		lblEffect = new JLabel("Effect:");
		GridBagConstraints gbc_lblEffect = new GridBagConstraints();
		gbc_lblEffect.insets = new Insets(0, 0, 5, 5);
		gbc_lblEffect.anchor = GridBagConstraints.WEST;
		gbc_lblEffect.gridx = 0;
		gbc_lblEffect.gridy = 0;
		rgbShiftPanel.add(lblEffect, gbc_lblEffect);
		
		rgbShiftColor = new JComboBox();
		rgbShiftColor.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(rgbShiftColor);
			}
		});
		rgbShiftColor.setModel(new DefaultComboBoxModel(new String[] {"RED", "GREEN", "BLUE", "VIBRANCE"}));
		GridBagConstraints gbc_rgbShiftColor = new GridBagConstraints();
		gbc_rgbShiftColor.insets = new Insets(0, 0, 5, 0);
		gbc_rgbShiftColor.fill = GridBagConstraints.HORIZONTAL;
		gbc_rgbShiftColor.gridx = 1;
		gbc_rgbShiftColor.gridy = 0;
		rgbShiftPanel.add(rgbShiftColor, gbc_rgbShiftColor);
		
		lblShiftAmount = new JLabel("Shift Amount:");
		GridBagConstraints gbc_lblShiftAmount = new GridBagConstraints();
		gbc_lblShiftAmount.anchor = GridBagConstraints.WEST;
		gbc_lblShiftAmount.insets = new Insets(0, 0, 0, 5);
		gbc_lblShiftAmount.gridx = 0;
		gbc_lblShiftAmount.gridy = 1;
		rgbShiftPanel.add(lblShiftAmount, gbc_lblShiftAmount);
		
		rgbShiftAmount = new JSlider();
		rgbShiftAmount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(rgbShiftAmount);
			}
		});
		rgbShiftAmount.setValue(5);
		rgbShiftAmount.setMinimum(1);
		GridBagConstraints gbc_rgbShiftAmount = new GridBagConstraints();
		gbc_rgbShiftAmount.anchor = GridBagConstraints.WEST;
		gbc_rgbShiftAmount.gridx = 1;
		gbc_rgbShiftAmount.gridy = 1;
		rgbShiftPanel.add(rgbShiftAmount, gbc_rgbShiftAmount);
		GridBagLayout gbl_dataAsSoundPanel = new GridBagLayout();
		gbl_dataAsSoundPanel.columnWidths = new int[]{0, 0, 0};
		gbl_dataAsSoundPanel.rowHeights = new int[]{0, 0};
		gbl_dataAsSoundPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_dataAsSoundPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		dataAsSoundPanel.setLayout(gbl_dataAsSoundPanel);
		
		lblBitrateDistort = new JLabel("Bitrate Distort:");
		GridBagConstraints gbc_lblBitrateDistort = new GridBagConstraints();
		gbc_lblBitrateDistort.anchor = GridBagConstraints.WEST;
		gbc_lblBitrateDistort.insets = new Insets(0, 0, 0, 5);
		gbc_lblBitrateDistort.gridx = 0;
		gbc_lblBitrateDistort.gridy = 0;
		dataAsSoundPanel.add(lblBitrateDistort, gbc_lblBitrateDistort);
		
		dataAsSoundBitRate = new JSlider();
		dataAsSoundBitRate.setInverted(true);
		dataAsSoundBitRate.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(dataAsSoundBitRate);
			}
		});
		dataAsSoundBitRate.setMaximum(9);
		dataAsSoundBitRate.setMinimum(1);
		dataAsSoundBitRate.setValue(5);
		GridBagConstraints gbc_dataAsSoundBitRate = new GridBagConstraints();
		gbc_dataAsSoundBitRate.gridx = 1;
		gbc_dataAsSoundBitRate.gridy = 0;
		dataAsSoundPanel.add(dataAsSoundBitRate, gbc_dataAsSoundBitRate);
		GridBagLayout gbl_randomPixelPanel = new GridBagLayout();
		gbl_randomPixelPanel.columnWidths = new int[]{0, 0, 0};
		gbl_randomPixelPanel.rowHeights = new int[]{0, 0, 0};
		gbl_randomPixelPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_randomPixelPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		randomPixelPanel.setLayout(gbl_randomPixelPanel);
		
		lblRandomization = new JLabel("Randomization:");
		GridBagConstraints gbc_lblRandomization = new GridBagConstraints();
		gbc_lblRandomization.anchor = GridBagConstraints.WEST;
		gbc_lblRandomization.insets = new Insets(0, 0, 5, 5);
		gbc_lblRandomization.gridx = 0;
		gbc_lblRandomization.gridy = 0;
		randomPixelPanel.add(lblRandomization, gbc_lblRandomization);
		
		randomRandomization = new JSlider();
		randomRandomization.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(randomRandomization);
			}
		});
		randomRandomization.setMinimum(5);
		GridBagConstraints gbc_randomRandomization = new GridBagConstraints();
		gbc_randomRandomization.insets = new Insets(0, 0, 5, 0);
		gbc_randomRandomization.gridx = 1;
		gbc_randomRandomization.gridy = 0;
		randomPixelPanel.add(randomRandomization, gbc_randomRandomization);
		
		lblNewLabel = new JLabel("Distortion Amount:");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		randomPixelPanel.add(lblNewLabel, gbc_lblNewLabel);
		
		randomDistortionAmount = new JSlider();
		randomDistortionAmount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(randomDistortionAmount);
			}
		});
		randomDistortionAmount.setMinimum(1);
		GridBagConstraints gbc_randomDistortionAmount = new GridBagConstraints();
		gbc_randomDistortionAmount.gridx = 1;
		gbc_randomDistortionAmount.gridy = 1;
		randomPixelPanel.add(randomDistortionAmount, gbc_randomDistortionAmount);
		GridBagLayout gbl_pixelSlicePanel = new GridBagLayout();
		gbl_pixelSlicePanel.columnWidths = new int[]{0, 0, 0};
		gbl_pixelSlicePanel.rowHeights = new int[]{0, 0};
		gbl_pixelSlicePanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_pixelSlicePanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		pixelSlicePanel.setLayout(gbl_pixelSlicePanel);
		
		lblDistortionAmount_1 = new JLabel("Distortion Amount:");
		GridBagConstraints gbc_lblDistortionAmount_1 = new GridBagConstraints();
		gbc_lblDistortionAmount_1.anchor = GridBagConstraints.WEST;
		gbc_lblDistortionAmount_1.insets = new Insets(0, 0, 0, 5);
		gbc_lblDistortionAmount_1.gridx = 0;
		gbc_lblDistortionAmount_1.gridy = 0;
		pixelSlicePanel.add(lblDistortionAmount_1, gbc_lblDistortionAmount_1);
		
		pixelSliceAmount = new JSlider();
		pixelSliceAmount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(pixelSliceAmount);
			}
		});
		pixelSliceAmount.setMaximum(9);
		pixelSliceAmount.setMinimum(1);
		pixelSliceAmount.setValue(6);
		GridBagConstraints gbc_pixelSliceAmount = new GridBagConstraints();
		gbc_pixelSliceAmount.gridx = 1;
		gbc_pixelSliceAmount.gridy = 0;
		pixelSlicePanel.add(pixelSliceAmount, gbc_pixelSliceAmount);
		GridBagLayout gbl_fractalSortPanel = new GridBagLayout();
		gbl_fractalSortPanel.columnWidths = new int[]{0, 0, 0};
		gbl_fractalSortPanel.rowHeights = new int[]{0, 0};
		gbl_fractalSortPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_fractalSortPanel.rowWeights = new double[]{0.0, Double.MIN_VALUE};
		fractalSortPanel.setLayout(gbl_fractalSortPanel);
		
		lblDistortionAmount = new JLabel("Distortion Amount:");
		GridBagConstraints gbc_lblDistortionAmount = new GridBagConstraints();
		gbc_lblDistortionAmount.anchor = GridBagConstraints.WEST;
		gbc_lblDistortionAmount.insets = new Insets(0, 0, 0, 5);
		gbc_lblDistortionAmount.gridx = 0;
		gbc_lblDistortionAmount.gridy = 0;
		fractalSortPanel.add(lblDistortionAmount, gbc_lblDistortionAmount);
		
		fractalSortDistortionAmount = new JSlider();
		fractalSortDistortionAmount.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(fractalSortDistortionAmount);
			}
		});
		fractalSortDistortionAmount.setMaximum(12);
		fractalSortDistortionAmount.setMinimum(2);
		fractalSortDistortionAmount.setValue(6);
		GridBagConstraints gbc_fractalSortDistortionAmount = new GridBagConstraints();
		gbc_fractalSortDistortionAmount.gridx = 1;
		gbc_fractalSortDistortionAmount.gridy = 0;
		fractalSortPanel.add(fractalSortDistortionAmount, gbc_fractalSortDistortionAmount);
		GridBagLayout gbl_pixelSortPanel = new GridBagLayout();
		gbl_pixelSortPanel.columnWidths = new int[]{0, 0, 0};
		gbl_pixelSortPanel.rowHeights = new int[]{0, 0, 0};
		gbl_pixelSortPanel.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_pixelSortPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		pixelSortPanel.setLayout(gbl_pixelSortPanel);
		
		lblDistortion = new JLabel("Distortion:");
		GridBagConstraints gbc_lblDistortion = new GridBagConstraints();
		gbc_lblDistortion.anchor = GridBagConstraints.WEST;
		gbc_lblDistortion.insets = new Insets(0, 0, 5, 5);
		gbc_lblDistortion.gridx = 0;
		gbc_lblDistortion.gridy = 0;
		pixelSortPanel.add(lblDistortion, gbc_lblDistortion);
		
		pixelSortBrightness = new JSlider();
		pixelSortBrightness.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(pixelSortBrightness);
			}
		});
		pixelSortBrightness.setMinimum(1);
		GridBagConstraints gbc_pixelSortBrightness = new GridBagConstraints();
		gbc_pixelSortBrightness.insets = new Insets(0, 0, 5, 0);
		gbc_pixelSortBrightness.gridx = 1;
		gbc_pixelSortBrightness.gridy = 0;
		pixelSortPanel.add(pixelSortBrightness, gbc_pixelSortBrightness);
		
		lblDistortionLength = new JLabel("Distortion Length:");
		GridBagConstraints gbc_lblDistortionLength = new GridBagConstraints();
		gbc_lblDistortionLength.insets = new Insets(0, 0, 0, 5);
		gbc_lblDistortionLength.gridx = 0;
		gbc_lblDistortionLength.gridy = 1;
		pixelSortPanel.add(lblDistortionLength, gbc_lblDistortionLength);
		
		pixelSortLength = new JSlider();
		pixelSortLength.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) 
			{
				GlitchArt.this.glitchUIManager.changeSliderValue(pixelSortLength);
			}
		});
		pixelSortLength.setValue(5);
		pixelSortLength.setMaximum(10);
		pixelSortLength.setMinimum(1);
		GridBagConstraints gbc_pixelSortLength = new GridBagConstraints();
		gbc_pixelSortLength.gridx = 1;
		gbc_pixelSortLength.gridy = 1;
		pixelSortPanel.add(pixelSortLength, gbc_pixelSortLength);
		layeredPane.setLayout(gl_layeredPane);
		contentPane.setLayout(gl_contentPane);
	}
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package reports;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.io.font.constants.StandardFonts;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.IBlockElement;
import com.itextpdf.layout.element.IElement;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.TableRenderer;

import modelo.minutas.Acuerdo;
import modelo.minutas.AreaOportunidad;
import modelo.minutas.Compromiso;
import modelo.minutas.Minuta;
import modelo.minutas.Participante;
import modelo.minutas.TemaMinuta;
import util.minutas.SigGen;

/**
 *
 * @author desarolloyc
 */
public class CrearPDF
{
	private Minuta		minuta;
	private String		ruta;

	/** The HTML-string that we are going to convert to PDF. */
	public String		HTML;
	/** The target folder for the result. */
	public String		TARGET;
	public String		TARGETHTML;
	public String		TARGETHTMLFirmas;
	public String		targetLogoSalud;
	public String		targetLogoSaludGris;
	public String		targetLogoCampeche;
	/** The path to the resulting PDF file. */
	public String		DESTFIRMAS;
	public String		BASEURI;
	private String[]	meses	= { "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto",
			"Septiembre", "Octubre", "Noviembre", "Diciembre" };

	public CrearPDF(Minuta minuta)
	{
		this.minuta = minuta;

		//Se establece la ruta de la minuta donde se generará
		ExternalContext ext = FacesContext.getCurrentInstance().getExternalContext();
		this.TARGET = ext.getRealPath(String.format("/resources/minutas/%s", this.minuta.getIdMinuta() + ".pdf"));
		this.TARGETHTML = ext.getRealPath(String.format("/resources/minutas/%s", this.minuta.getIdMinuta() + ".html"));
		this.TARGETHTMLFirmas = ext
				.getRealPath(String.format("/resources/minutas/%s", this.minuta.getIdMinuta() + "firmas.html"));
		this.targetLogoSalud = ext.getRealPath(String.format("/resources/minutas/css/%s", "saludlogo.jpeg"));
		this.targetLogoSaludGris = ext.getRealPath(String.format("/resources/minutas/css/%s", "footer1.png"));
		this.targetLogoCampeche = ext.getRealPath(String.format("/resources/minutas/css/%s", "escudologo.png"));

		this.DESTFIRMAS = ext.getRealPath("/resources/minutas/firmasm" + this.minuta.getIdMinuta() + "/");

		File destinoFirmas = new File(this.DESTFIRMAS);
		destinoFirmas.mkdirs();

		this.BASEURI = ext.getRealPath("/resources/minutas/");

	}

	public void createHTML()
	{
		//Crea las firmas
		if (this.minuta.getParticipantes() != null && !this.minuta.getParticipantes().isEmpty())
		{
			for (Participante part : this.minuta.getParticipantes())
			{

				try
				{
					File archivo = new File(this.DESTFIRMAS + "f" + part.getIdParticipante() + ".png");

					SigGen.generateSignature(part.getFirma(), new FileOutputStream(archivo));
				}
				catch (FileNotFoundException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		}

		File archivo = new File(this.TARGETHTML);
		File archivoFirmas = new File(this.TARGETHTMLFirmas);

		if (archivo.exists())
		{
			archivo.delete();
		}

		if (archivoFirmas.exists())
		{
			archivoFirmas.delete();
		}

		String htmlFinal = "<html>\n" + "\n" + "<head>\n" + "<title>Minuta No. " + this.minuta.getIdMinuta()
				+ "</title>\n" + "<meta name=\"description\" content=\"Minuta No. " + this.minuta.getIdMinuta()
				+ "\" />\n" + "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/pdf.css\">\n" + "</head>\n" + "\n"
				+ "<body> <h2>MINUTA</h2><h2 style='border-bottom: 25px solid white;'>" + this.minuta.getDescripcion()
				+ "</h2>";

		htmlFinal += getPresentacion() + "<p> </p>";

		if (!this.minuta.getTemas().isEmpty())
		{
			htmlFinal += "<ol>";

			for (TemaMinuta tema : this.minuta.getTemas())
			{
				htmlFinal += "<li>" + tema.getDescripcion() + " ( "
						+ tema.getResponsable().getPersona().getNombreCompletoYCargo() + " ) " + "</li>";
			}

			htmlFinal += "</ol>";

		}

		if (this.minuta.getIntroduccion() != null && !this.minuta.getIntroduccion().isEmpty())
		{

			htmlFinal += "<br/>" + this.minuta.getIntroduccion().trim() + "<br/>";
		}

		if (!this.minuta.getTemas().isEmpty())
		{

			htmlFinal += "<h1>Desarrollo de los Temas</h1>" + "<ol>";

			for (TemaMinuta tema : this.minuta.getTemas())
			{
				htmlFinal += "<li>" + tema.getDesarrollo() + "</li>";
			}

			htmlFinal += "</ol>";

		}

		if (!this.minuta.getCompromisos().isEmpty() || !this.minuta.getAcuerdos().isEmpty()
				|| !this.minuta.getAreasOportunidad().isEmpty())
		{
			//	htmlFinal += "<p><h1>Derivado de la presente minuta, se observa lo siguiente: </h1></p>";
		}

		if (!this.minuta.getAcuerdos().isEmpty())
		{

			htmlFinal += "<h1>Acuerdos</h1>";
			htmlFinal += "<ol>";

			for (Acuerdo acuerdo : this.minuta.getAcuerdos())
			{

				htmlFinal += "<li>" + acuerdo.getDescripcion()
						+ (acuerdo.getObservaciones() != null && !acuerdo.getObservaciones().isEmpty()
								? ", " + acuerdo.getObservaciones()
								: "")
						+ ".</li>";
			}

			htmlFinal += "</ol>";
		}

		if (!this.minuta.getCompromisos().isEmpty())
		{
			htmlFinal += "<h1>Compromisos</h1>";
			htmlFinal += "<ol>";

			LocalDate ldt = null;
			String fFinalizacion = null;

			for (Compromiso compromiso : this.minuta.getCompromisos())
			{
				if (compromiso.getFechaFinalizacionEstimada() != null)
				{
					System.out.print(compromiso.getFechaFinalizacionEstimada());
					compromiso.updateFechaFinalizacionEstimadaString();

					DateTimeFormatter dateformatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					ldt = LocalDate.parse(compromiso.getFechaFinalizacionEstimadaString(), dateformatter);

					fFinalizacion = compromiso.getFechaFinalizacionEstimada() != null
							? ", fecha compromiso: " + ldt.getDayOfMonth() + " de "
									+ this.meses[ldt.getMonthValue() - 1] + " de " + ldt.getYear()
							: "";
				}

				htmlFinal += "<li>"
						+ (compromiso.getResponsable() != null
								? compromiso.getResponsable().getPersona().getNombreCompleto() + " - "
								: "")
						+ compromiso.getDescripcion() + (fFinalizacion != null ? fFinalizacion : "") + ".</li>";
			}

			htmlFinal += "</ol>";
		}

		if (!this.minuta.getAreasOportunidad().isEmpty())
		{
			htmlFinal += "<h1>Áreas de Oportunidad</h1>";
			htmlFinal += "<ol>";

			for (AreaOportunidad areaOportunidad : this.minuta.getAreasOportunidad())
			{
				htmlFinal += "<li>" + areaOportunidad.getDescripcion()
						+ (!areaOportunidad.getPropuestaSolucion().isEmpty()
								? ". " + areaOportunidad.getPropuestaSolucion()
								: "")
						+ ".</li>";
			}

			htmlFinal += "</ol>";
		}

		/*
		 * if (this.minuta.getConclusion() != null &&
		 * !this.minuta.getConclusion().isEmpty()) { htmlFinal +=
		 * this.minuta.getConclusion(); }
		 */

		if (this.minuta.getTipoMinuta().getIdTipoMinuta() == 0)
		{
			htmlFinal += "<p>De esta forma se da por terminada la presente reunión, firmando de conformidad todas las partes que intervienen.</p><br></br>";
		}

		htmlFinal += "\n" + "</body>\n" + "\n" + "</html>";

		String htmlFinalFirmas = " <html>\n" + "\n" + "<head>\n"
				+ "<link rel=\"stylesheet\" type=\"text/css\" href=\"css/pdf.css\">\n" + "</head>\n" + "\n" + "<body> ";

		htmlFinalFirmas += "<h1>Firmas de los participantes.</h1>";

		if (this.minuta.getParticipantes() != null && !this.minuta.getParticipantes().isEmpty())
		{
			htmlFinalFirmas += "<table>";

			int columnas = 1;

			for (Participante part : this.minuta.getParticipantes())
			{
				if (columnas == 1)
				{
					htmlFinalFirmas += "<tr>";
				}

				htmlFinalFirmas += "<td >" + part.getPersona().getTitulo() + " " + part.getPersona().getNombreCompleto()
						+ "   <img src=\"firmasm" + this.minuta.getIdMinuta() + "/" + "f" + part.getIdParticipante()
						+ ".png" + "\">\n" + "   \n" + "    " + part.getPersona().getCargo() + " \n" + "    </td>\n";

				if (columnas == 3)
				{
					htmlFinalFirmas += "</tr>";
					columnas = 1;
					continue;
				}

				columnas++;

			}

			if (columnas == 3)
			{
				htmlFinalFirmas += "</tr>";

			}

			htmlFinalFirmas += "</table>";

		}

		htmlFinalFirmas += "\n" + "</body>\n" + "\n" + "</html>";

		try (BufferedWriter bw = new BufferedWriter(new FileWriter(archivo));
				BufferedWriter bwFirmas = new BufferedWriter(new FileWriter(archivoFirmas)))
		{
			bw.write(htmlFinal);
			bwFirmas.write(htmlFinalFirmas);

			bw.close();
			bwFirmas.close();

		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public void createPdf() throws IOException
	{
		createHTML();

		// Create a PdfFont
		PdfFont font = PdfFontFactory.createFont(StandardFonts.HELVETICA);
		PdfFont bold = PdfFontFactory.createFont(StandardFonts.HELVETICA_BOLD);
		PdfFont fontFooter = PdfFontFactory.createFont(StandardFonts.HELVETICA);

		ConverterProperties properties = new ConverterProperties();
		properties.setBaseUri(this.BASEURI);

		PdfDocument pdf = new PdfDocument(new PdfWriter(this.TARGET));
		Document document = new Document(pdf, PageSize.LETTER);
		document.setMargins(75, 85, 75, 85);

		//HEADER
		pdf.addEventHandler(PdfDocumentEvent.START_PAGE,
				new TableHeaderEventHandler(document, this.targetLogoSalud, this.targetLogoCampeche));

		//FOOTER
		Table table = new Table(1);
		table.setWidth(523);

		Cell celdaEnBlanco = new Cell().add(new Paragraph("INDESALUD"));

		Cell celdaTexto = new Cell()
				.add(new Paragraph("INDESALUD\n" + "Calle 10 Num. 286 A\\n" + "Barrio de San Román, C.P. 24040\n"
						+ "San Francisco de Campeche, Campeche\n" + "Tel. (981)81-19870\n" + "www.campeche.gob.mx\n")
								.setFont(fontFooter).setFontSize(7).setHorizontalAlignment(HorizontalAlignment.RIGHT));

		Image saludGrid = new Image(ImageDataFactory.create(this.targetLogoSaludGris)).setHeight(70);

		Cell celdaLogoSalud = new Cell();
		Paragraph p = new Paragraph("");
		p.add(saludGrid);
		p.setTextAlignment(TextAlignment.RIGHT);
		celdaLogoSalud.add(p);
		celdaLogoSalud.setBorder(Border.NO_BORDER);

		table.addCell(celdaLogoSalud);
		//		table.addCell(celdaTexto);
		//		table.addCell(celdaLogoSalud);

		pdf.addEventHandler(PdfDocumentEvent.END_PAGE, new TableFooterEventHandler(table));

		List<IElement> elements = HtmlConverter.convertToElements(new FileInputStream(TARGETHTML), properties);
		List<IElement> elementsFirmas = HtmlConverter.convertToElements(new FileInputStream(TARGETHTMLFirmas),
				properties);

		//Añadir introducción
		for (IElement element : elements)
		{
			document.add((IBlockElement) element).setFont(bold);
		}

		document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));

		//Añadir Firmas
		for (IElement elementFirmas : elementsFirmas)
		{
			document.add((IBlockElement) elementFirmas).setFont(bold);
		}

		document.close();

	}

	private String getPresentacion()
	{
		String texto = "";
		String participantesString = "";

		List<Participante> participantes = this.minuta.getParticipantes();

		for (int x = 0; x < participantes.size(); x++)
		{
			Participante par = participantes.get(x);

			if (x == participantes.size() - 1)
			{
				participantesString = (participantes.size() > 1
						? participantesString.substring(0, participantesString.length() - 1) + " y "
						: "") + (par.getPersona().getSexo().equalsIgnoreCase("m") ? "el " : "la ")
						+ par.getPersona().getTitulo() + " " + par.getPersona().getNombreCompletoYCargo();

			}
			else
			{
				participantesString += (par.getPersona().getSexo().equalsIgnoreCase("m") ? "el " : "la ")
						+ par.getPersona().getTitulo() + " " + par.getPersona().getNombreCompletoYCargo() + ", ";
			}
		}

		LocalDateTime ldt = LocalDateTime.ofInstant(this.minuta.getFechaHora().toInstant(), ZoneId.systemDefault());

		if (this.minuta.getTipoMinuta().getIdTipoMinuta() == 0)
		{
			texto = "<p>Siendo las " + ldt.getHour() + ":" + ldt.getMinute() + " hrs. del día " + ldt.getDayOfMonth()
					+ " del mes de " + this.meses[ldt.getMonthValue() - 1] + " del " + ldt.getYear()
					+ ", estando reunidos en " + this.minuta.getLugar();
			texto += ", se contó con la presencia de ";
			texto += participantesString;

			if (this.minuta.getTemas().size() > 0)
			{
				texto += ", para la revisión de los temas que se enlistan a continuación y dar cumplimiento a los acuerdos y"
						+ " compromisos del siguiente orden del día:";
			}
			else
			{
				texto += ", donde se llegó a los siguientes acuerdos y/o compromisos.";
			}

			texto += " Se anexa listado de firmas.";

		}
		else
		{
			texto = "<p>Derivado de la Gira de Trabajo realizada el día " + ldt.getDayOfMonth() + " del mes de "
					+ this.meses[ldt.getMonthValue() - 1] + " del " + ldt.getYear() + ", en " + this.minuta.getLugar()
					+ " con motivo de " + this.minuta.getMotivoVisita() + ", donde se reunieron: " + participantesString
					+ ". Se anexa listado de firmas.</p>";
		}

		return texto;

	}

	public class TableHeaderEventHandler implements IEventHandler
	{
		protected Table		table;
		protected float		tableHeight;
		protected float		tableWidth;
		protected Document	doc;

		public TableHeaderEventHandler(Document doc, String rutaLogoSalud, String rutaLogoCampeche)
		{
			this.doc = doc;
			table = new Table(2);
			table.setWidth(465);

			try
			{
				Image salud = new Image(ImageDataFactory.create(rutaLogoSalud)).setHeight(60);
				Image escudo = new Image(ImageDataFactory.create(rutaLogoCampeche)).setHeight(60);

				Cell celdaEscudoCampeche = new Cell();
				celdaEscudoCampeche.setBorder(Border.NO_BORDER);
				celdaEscudoCampeche.add(escudo);
				celdaEscudoCampeche.setHorizontalAlignment(HorizontalAlignment.LEFT);

				Cell celdaLogoSalud = new Cell();
				celdaLogoSalud.setBorder(Border.NO_BORDER);

				Paragraph p = new Paragraph();
				p.add(salud);
				p.setTextAlignment(TextAlignment.RIGHT);
				celdaLogoSalud.add(p).setHorizontalAlignment(HorizontalAlignment.RIGHT);

				table.addCell(celdaEscudoCampeche);
				table.addCell(celdaLogoSalud);

				TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
				renderer.setParent(
						new Document(new PdfDocument(new PdfWriter(new ByteArrayOutputStream()))).getRenderer());
				tableHeight = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.LETTER))).getOccupiedArea()
						.getBBox().getHeight();
				tableWidth = PageSize.LETTER.getWidth();
			}
			catch (MalformedURLException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		public void handleEvent(Event event)
		{
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdfDoc = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
			Rectangle rect = new Rectangle(pdfDoc.getDefaultPageSize().getX() + (doc.getLeftMargin() - 12),
					pdfDoc.getDefaultPageSize().getTop() - doc.getTopMargin(), getTableWidth() - doc.getRightMargin(),
					getTableHeight());
			new Canvas(canvas, pdfDoc, rect).add(table);
		}

		public float getTableHeight()
		{
			return tableHeight;
		}

		public float getTableWidth()
		{
			return tableWidth;
		}

	}

	protected class TableFooterEventHandler implements IEventHandler
	{
		private Table table;

		public TableFooterEventHandler(Table table)
		{
			this.table = table;
		}

		@Override
		public void handleEvent(Event event)
		{
			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
			PdfDocument pdfDoc = docEvent.getDocument();
			PdfPage page = docEvent.getPage();
			PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
			new Canvas(canvas, pdfDoc, new Rectangle(25, 20, page.getPageSize().getWidth() - 72, 80)).add(table);
		}
	}

	public String getRuta()
	{
		return ruta;
	}

	public void setRuta(String ruta)
	{
		this.ruta = ruta;
	}

}

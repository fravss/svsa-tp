package gaian.svsa.ep.service;

import java.io.Serializable;

import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.List;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

import gaian.svsa.ep.model.Ocorrencia;
import gaian.svsa.ep.util.DateUtils;
import gaian.svsa.ep.util.NegocioException;
import lombok.extern.log4j.Log4j;




@Log4j
public class OcorrenciaPDFService implements Serializable {

	private static final long serialVersionUID = 1L;
	public ByteArrayOutputStream generateStream(List<Ocorrencia> ocorrencias) throws NegocioException {

		try {
			//gerando documento
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter writer = new PdfWriter(baos);
			PdfDocument pdf = new PdfDocument(writer);
			pdf.setDefaultPageSize(PageSize.A4);
			Document document = new Document(pdf);
			//margem
			document.setMargins(72, 50, 50, 50);

			generateContent(pdf, document, ocorrencias);
			
			document.close();

			log.info("documento feito");

			return baos;

		} catch (Exception ex) {

			log.error("error: " + ex.getMessage());

			throw new NegocioException("Erro na montagem do PDF: " + ex.getMessage());

		}

	}

	private void generateContent(PdfDocument pdfDocument, Document document, List<Ocorrencia> ocorrencias)
			throws Exception, MalformedURLException {

		log.info("header feito");

		String logo = ("logoSalto.png");

		String logoSvsa = ("SVSA_Transparente.png");

		try {
			//logo Prefeitura
			String logoSaltoPath = getClass().getClassLoader().getResource("imagens/" + logo).toString();

			//logo SVSA
			String logosvsaPath = getClass().getClassLoader().getResource("imagens/" + logoSvsa).toString();
            //cabeçalho
			pdfDocument.addEventHandler(PdfDocumentEvent.START_PAGE,new HeaderEventHandler(logoSaltoPath, logosvsaPath));

		} catch (NullPointerException e) {

			System.err.println("Imagem não encontrada no caminho especificado.");

			e.printStackTrace();

		} catch (Exception e) {

			System.err.println("Erro ao carregar a imagem: " + e.getMessage());

			e.printStackTrace();

		}

		Paragraph line = new Paragraph("Ocorrências\n");

		line.setFontSize(24);

		line.setTextAlignment(TextAlignment.CENTER);

		document.add(line);

		tabelaOcorrencia(document, ocorrencias);
	}

	private static class HeaderEventHandler implements IEventHandler {

		private Image imageLogoSalto;

		private Image imageLogosvsa;

		public HeaderEventHandler(String logoSaltoPath, String logosvsaPath) throws MalformedURLException {

			ImageData imageLogoSalto = ImageDataFactory.create(logoSaltoPath);

			this.imageLogoSalto = new Image(imageLogoSalto);

			ImageData imageLogosvsa = ImageDataFactory.create(logosvsaPath);

			this.imageLogosvsa = new Image(imageLogosvsa);

		}

		@Override

		public void handleEvent(Event event) {

			// Acessando o evento como um PdfDocumentEvent

			PdfDocumentEvent docEvent = (PdfDocumentEvent) event;

			PdfPage page = docEvent.getPage();


			// Caminho para o logotipo

			try {

				this.imageLogoSalto.scaleToFit(100f, 150f);

				this.imageLogoSalto.setFixedPosition(40, page.getPageSize().getTop() - 60);

				this.imageLogosvsa.scaleToFit(100f, 150f);

				this.imageLogosvsa.setFixedPosition(150, page.getPageSize().getTop() - 70);

				Canvas canvas = extracted(page).add(imageLogoSalto);

				canvas.add(imageLogosvsa);

				PdfCanvas pdfCanvas = new PdfCanvas(page);

				pdfCanvas.moveTo(30, 774)

						.lineTo(565, 774)

						.setLineWidth(1.5f)

						.stroke();
			
				canvas.close();
	

			} catch (Exception e) {

				e.printStackTrace();

			}

		}

		private Canvas extracted(PdfPage page) {
			return new Canvas(new PdfCanvas(page), page.getPageSize());
		}

	}

	public void tabelaOcorrencia(Document document, List<Ocorrencia> ocorrencias) {

		log.info("criando tabela");

		for (Ocorrencia Ocorrencia : ocorrencias) {

			float[] columnWidths = new float[] { 0.2f, 0.2f, 0.2f};

			Table pTable = new Table(UnitValue.createPercentArray(columnWidths));

			pTable.setWidth(UnitValue.createPercentValue(100));

			Cell cellRemetente = new Cell(1, 2)

					.add(new Paragraph("Remetente: " + Ocorrencia.getRemetente().getNome()))

					.setPadding(5f);

			pTable.addCell(cellRemetente);

			Cell cellTipo = new Cell(2, 1)

					.add(new Paragraph("Tipo Ocorrência: " + Ocorrencia.getTipo()))
					
					.setBackgroundColor(ColorConstants.LIGHT_GRAY)

					.setPadding(5f);

			pTable.addCell(cellTipo);
			
			Cell cellDestinatario = new Cell(1, 2)

					.add(new Paragraph("Destinatário: " + Ocorrencia.getDestinatario().getNome()))

					.setPadding(5f);

			pTable.addCell(cellDestinatario);	

			pTable.addCell(new Cell(1, 1).add(new Paragraph("Status: " + Ocorrencia.getStatus())));
			
			pTable.addCell(new Cell(1, 1).add(new Paragraph("Unidade: " + Ocorrencia.getUnidade().getNome())));			
			try {
			  
			 String dataFormatada = DateUtils.parseDateToString(Ocorrencia.getDataCriacao());
			 pTable.addCell(new Cell().add(new Paragraph("Data: " + dataFormatada)));
			 
			  } catch (ParseException e) {
			  
			  e.printStackTrace();
			  
			  pTable.addCell(new Cell().add(new Paragraph("Data: inválida")));
		 
			  }
			 
			pTable.addCell(new Cell(1, 3).add(new Paragraph("Descrição: " + Ocorrencia.getDescricao())));
			
			pTable.setMarginBottom(10f);
			
			pTable.setKeepTogether(true);

			document.add(pTable);

		}

	}

}
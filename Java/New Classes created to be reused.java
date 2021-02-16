public class GeraBook {
	
	String emprId, cartId, dtIni, dtFin;
	
	GeraBook(String emprId, String cartId, String dtIni, String dtFin){
		this.emprId = emprId;
		this.cartId = cartId;
		this.dtIni = dtIni;
		this.dtFin = dtFin;
	}

	public JasperPrint[] getBook() {		

		ArrayList<String> carteiras = new ArrayList<>();
		carteiras.add(cartId);
		
		String path = getWorkbookPath();

		HashMap<String, Object> parameters = new HashMap<>();
		HashMap<String, String> tabelasTemporarias = QueriesUtil.obterTabelasTemporarias();

		if (QueriesUtil.carregar_TabelasTemporarias(emprId, cartId, tabelasTemporarias, carteiras, dtIni, dtFin)) {
			//Gera parametros		
			parameters.putAll(tabelasTemporarias);

			parameters.put("data_inicial", QueriesUtil.obterDataInicial(tabelasTemporarias.get("cli_sel").toString()));
			parameters.put("cotDolar", QueriesUtil.obterdolar(tabelasTemporarias.get("cli_sel").toString()));
			parameters.put("cotEuro", QueriesUtil.obtereuro(tabelasTemporarias.get("cli_sel").toString()));
			parameters.put("tot_pg", new Integer(0)); 
			parameters.put("cab_rodape", ConnectBook.retornaCampoSQL("empr_book_rodape", "rc_empresas", " empr_id=" + Parametros.empr_id_SQL)); 
			parameters.put("cab_fantasia", ConnectBook.retornaCampoSQL("empr_book_fantasia", "rc_empresas", " empr_id=" + Parametros.empr_id_SQL));
			parameters.put("BASE_DIR", path);
			parameters.put("IMG_LOGO", "img/logo/book/logo.png");
			parameters.put("IMG_TOPO_CAPA", "img/logo/book/Legend.jpg");
			parameters.put("empr", new Integer(emprId));
			parameters.put("cart", new Integer(cartId));

			//Gera jpreload
			File jasperPreLoad = new File(path+"/preload.jasper");
			InputStream jpreload = null;
			try {
				jpreload = new FileInputStream(jasperPreLoad);
			} catch (FileNotFoundException e1) {
				Comm.msgERRO("Não foi possível carregar o arquivo de preload.jasper!\nFavor contactar o suporte ABS.");
				e1.printStackTrace();
			}

			//Gera paginas
			List<Map<String, Object>> pages = new ArrayList<>();
			ResultSet rs = getPaginasRelatorio();

			while (!Connect_rst.eofSQL(rs)){
				try {
					Map<String, Object> pageMap = new HashMap<>();				
					File relaPagina = new File(path+ "/" + Connect.leDadosSQL(rs, "repa_jasper"));	

					pageMap.put("jasper", new FileInputStream(relaPagina));
					pageMap.put("titulo",Connect.leDadosSQL(rs, "repa_titulo_pagina"));
					pageMap.put("disclaimer",Connect.leDadosSQL(rs, "repa_disclaimer"));

					pages.add(pageMap);

				} catch (FileNotFoundException e) {
					Comm.msgERRO("Erro ao emitir o relatório!\nPágina " + Connect.leDadosSQL(rs, "repa_titulo_pagina") + " não encontrada.");
					e.printStackTrace();
				}
				Connect_rst.nextSQL(rs);
			}
			
			//o método GeraJasper.getJasperPrints() não pode ser passado diretamente no return pois senão não será possível deletar as tabelas temporárias a seguir
			JasperPrint[] jasperPrints = GeraJasper.getJasperPrints(parameters, pages, jpreload, ConnectBook.getConnection());

			if(!Parametros.debugQuery){
				QueriesUtil.deletar_TabelasTemporarias(tabelasTemporarias);
			}

			return jasperPrints;

		}
		return null;
	}

	private String getWorkbookPath() {
		return Connect_rst.retornaCampoSQL(
				//select
				"distinct rela_workbook_path", 
				//from
				"rc_relatorios r\n" + 
				"inner join rc_book_perfil_relatorios_paginas pr\n" + 
				"		on pr.rela_id = r.rela_id\n" + 
				"inner join rc_book_perfil_carteiras pk\n" + 
				"		on pk.bper_id = pr.bper_id", 
				//where
				"pk.empr_id = "+emprId+"\n" + 
				"and pk.cart_id = "+cartId);
	}

	private ResultSet getPaginasRelatorio() {
		StrSQL strSQLRelatorios = new StrSQL();

		strSQLRelatorios.add("DECLARE @dt_inicio VARCHAR(10)");
		strSQLRelatorios.add("	, @empr_id INT");
		strSQLRelatorios.add("	, @cart_id INT 	");
		strSQLRelatorios.add("	");
		strSQLRelatorios.add("SELECT @empr_id = " + emprId);
		strSQLRelatorios.add("	 , @cart_id = " + cartId);
		strSQLRelatorios.add("");
		strSQLRelatorios.add("SELECT @dt_inicio = CONVERT(VARCHAR(10), MIN(hcar_dia), 103)");
		strSQLRelatorios.add("FROM dbo.rc_hist_cart_dia h");
		strSQLRelatorios.add("WHERE h.empr_id = @empr_id");
		strSQLRelatorios.add("  AND h.cart_id = @cart_id");
		strSQLRelatorios.add("");
		strSQLRelatorios.add("select rp.rela_id,");
		strSQLRelatorios.add("	   rp.repa_id,");
		strSQLRelatorios.add("	   rp.repa_nome,");
		strSQLRelatorios.add("	   rp.repa_ativo_sn,");
		strSQLRelatorios.add("	   rp.repa_ordem,");
		strSQLRelatorios.add("	   REPLACE(rp.repa_disclaimer, '<data_inicio>', @dt_inicio) repa_disclaimer,");
		strSQLRelatorios.add("	   rp.repa_jasper,");
		strSQLRelatorios.add("	   rp.repa_titulo_pagina ");
		strSQLRelatorios.add("from rc_relatorios_paginas rp ");
		strSQLRelatorios.add("inner join rc_book_perfil_relatorios_paginas bprp ");
		strSQLRelatorios.add("	on  bprp.repa_id = rp.repa_id and bprp.rela_id = rp.rela_id ");
		strSQLRelatorios.add("inner join rc_book_perfil_carteiras bpc ");
		strSQLRelatorios.add("	on  bpc.bper_id = bprp.bper_id ");
		strSQLRelatorios.add("inner join rc_carteiras c ");
		strSQLRelatorios.add("	on  c.empr_id = bpc.empr_id and c.cart_id = bpc.cart_id ");
		strSQLRelatorios.add("where bpc.empr_id = @empr_id and bpc.cart_id = @cart_id ");
		strSQLRelatorios.add("  and rp.repa_ativo_sn = 's' ");
		strSQLRelatorios.add("order by rp.repa_ordem");

		return Connect.executeResultSQL(strSQLRelatorios);
	}
}


public class GeraJasper {
	/**
	 * 
	 * @param parameters - Mapa de parâmetros a ser passado para o relatório jasper
	 * @param paginas - Lista de mapa das páginas do relatório.
	 * <br/>Cada mapa contendo o InputStream do jasper + titulo + disclaimer da página
	 * @param jpreload - jpreload usado para carga inicial do relatório
	 * @param connection - conexão que deve ser usada pelos relatórios acessarem o banco de dados
	 * @return Array de JasperPrints para ser usado como necessário para montar relatórios
	 */
	public static JasperPrint[] getJasperPrints(
			Map<String, Object> parameters, 
			List<Map<String, Object>> paginas, 
			InputStream jpreload,
			Connection connection) {		

		String relatorioAtual = "";

		try {
			List<JasperPrint> jasperPrinters = new ArrayList<JasperPrint>(); 

			int totalPaginas = 0;
			int numIndice = 0;

			JasperFillManager.fillReport(jpreload, parameters, connection);


			for (Map<String, Object>pagina : paginas){
				InputStream is = (InputStream) pagina.get("jasper");					
				relatorioAtual = (String) pagina.get("titulo");

				JasperPrint rel = JasperFillManager.fillReport(is, parameters, connection);

				numIndice = numIndice+rel.getPages().size();
				totalPaginas += rel.getPages().size();

				parameters.put("tot_pg", new Integer(totalPaginas)+1);//começar pag + 1 por causa merge comentários
				parameters.put("titulo",pagina.get("titulo"));
				parameters.put("textodisclaimer",pagina.get("disclaimer"));

				jasperPrinters.add(rel);

			}
			relatorioAtual = "";

			return jasperPrinters.toArray(new JasperPrint[jasperPrinters.size()]);

		} 
		catch (Exception ex) {
			ex.printStackTrace();
			String msg = "Erro ao emitir relatório.\nVerificar o arquivo do log (abs_err.log) para maiores detalhes";
			if (!relatorioAtual.equals(""))
				msg += "\nReport: " + relatorioAtual;

			JOptionPane.showMessageDialog(
					null,
					msg,
					"ABS Portfolio detectou o seguinte Erro:",
					JOptionPane.ERROR_MESSAGE);

			return null;
		}
	}
}
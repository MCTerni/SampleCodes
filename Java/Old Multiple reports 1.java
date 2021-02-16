	public void processa(){
		/*****Untouched code****/
		else if(msg==100){
			this.pLbStatus.setVisible(true);
			this.pBuExecuta.setEnabled(false);
			jProgressBarWorkbook.setVisible(true);


			pLbStatus.setText("Carregando tabelas...");
			this.setProgresso(14);
			if(QueriesUtil.carregar_TabelasTemporarias("1","0",tabelasTemporarias,carteiras,Comm.formataSQL(pPnData1.getText(),"D3"),Comm.formataSQL(pPnData2.getText(),"D3"))==true)
			{
				this.setProgresso(6);
				int incrementoRelatorios = 80/carteiras.size();
				boolean criouIndice = false;
				HashMap parametrosComuns;
				String relatorioAtual = "";

				for(int i=0; i<carteiras.size(); i++)
				{
					String cart_id = carteiras.get(i).toString();
					try{
						Map parameters = new HashMap();
						String retorno = null;
						String canto = null;
						String logo = ConnectBook.retornaCampoSQL("empr_logo", "rc_empresas", "empr_id = " + Parametros.empr_id_SQL);

						if (!logo.equalsIgnoreCase("")) {
							retorno = "pci/img/logo/book/" + logo;
							canto = "pci/img/logo/book/canto_" + logo;
						}


						parameters.put("BASE_DIR", "pci/jrv/workbook/Compilados");
						parameters.put("IMG_LOGO", "pci/img/logo/book/logo.png");
						parameters.put("IMG_TOPO_CAPA", "pci/img/logo/book/Legend.jpg");
						parameters.put("IMG_BOOK_CAPA", "pci/img/logo/book/logoLegend.png");
						parameters.put("IMG_CANTO_CAPA", "pci/img/logo/book/logoLegend.png");  // "pci/img/logo/book/gerval.jpg");
						parameters.put("IMG_LOGO_CAB", "pci/img/logo/book/garin_cab.jpg");
						parameters.put("IMG_LOGO_CAB_FUNDO", "pci/img/logo/book/garin_logo_fundo.jpg");
						parameters.put("IMG_CABECALHO", retorno);
						parameters.put("IMG_ANBIMA", "pci/img/logo/book/anbima.jpg");
						parameters.put("IMG_BRADESCO", "pci/img/logo/book/img_bradesco.jpg");
						parameters.put("IMG_CANTO", retorno);
						parameters.put("empr",new Integer(1));
						parameters.put("cart",new Integer(0));
						StrSQL strSQLCartTipoMoeda = new StrSQL();
						strSQLCartTipoMoeda.add("select c.cart_tipo,m.moed_nickname from rc_carteiras c,rc_moedas m where c.moed_id = m.moed_id and cart_id ="+ Comm.formataSQL(pTaFec.getValueCod2(i,0),"IN"));
						ResultSet rss = Connect.executeResultSQL(strSQLCartTipoMoeda);
						String cart_tipo = Connect.leDadosSQL(rss, "cart_tipo");
						parameters.put("cart_tipo", cart_tipo);
						String moed_nickname = Connect.leDadosSQL(rss,"moed_nickname");
						parameters.put("moed_nickname", moed_nickname);						
						parameters.put("datini", pPnData1.getValue());
						parameters.put("dt_fin", pPnData2.getValue());
						parameters.put("idx1", new Integer(2));
						parameters.put("idx2", new Integer(1));
						parameters.put("stemp_pg2", tabelasTemporarias.get("stemp_pg2"));
						parameters.put("stemp_pg3", tabelasTemporarias.get("stemp_pg3"));
						parameters.put("stemp_pg6", tabelasTemporarias.get("stemp_pg6"));
						parameters.put("posicao", tabelasTemporarias.get("posicao"));
						parameters.put("cli_sel", tabelasTemporarias.get("cli_sel"));
						parameters.put("cli_sel_tot", tabelasTemporarias.get("cli_sel"));
						parameters.put("temp_comportamento_retornos", tabelasTemporarias.get("temp_comportamento_retornos"));
						parameters.put("evo_patrimonio", tabelasTemporarias.get("evo_patrimonio"));
						parameters.put("stempd1", tabelasTemporarias.get("stempd1"));
						parameters.put("pos", tabelasTemporarias.get("pos"));
						parameters.put("stemp11", tabelasTemporarias.get("stemp11"));
						parameters.put("his", tabelasTemporarias.get("his"));
						parameters.put("comp_ir", tabelasTemporarias.get("comp_ir"));
						parameters.put("temp_ranking", tabelasTemporarias.get("temp_ranking"));
						parameters.put("temp_ranking_fundos", tabelasTemporarias.get("temp_ranking_fundos"));
						parameters.put("cli_selbank", tabelasTemporarias.get("cli_selbank"));
						parameters.put("pag2_parte1", tabelasTemporarias.get("pag2_parte1"));
						parameters.put("stemp_graf2_pg2", tabelasTemporarias.get("stemp_graf2_pg2"));
						parameters.put("stemp_graf2_pg2_aux_seq", tabelasTemporarias.get("stemp_graf2_pg2_aux_seq"));
						parameters.put("data_inicial", QueriesUtil.obterDataInicial(tabelasTemporarias.get("cli_sel").toString()));

						parameters.put("cotDolar",
								QueriesUtil.obterdolar(tabelasTemporarias.get("cli_sel").toString()));
						parameters.put("cotEuro", QueriesUtil.obtereuro(tabelasTemporarias.get("cli_sel").toString()));
						parameters.put("moeda", moed_nickname);	

						parametrosComuns = QueriesUtil.obterParametrosComuns(tabelasTemporarias.get("cli_sel").toString(),Comm.formataSQL(pPnData1.getText(),"D3"),Comm.formataSQL(pPnData2.getText(),"D3"));

						parameters.put("periodo_ini", parametrosComuns.get("datini"));
						parameters.put("periodo_fim", parametrosComuns.get("dat_fin"));
						parameters.put("clie_id", parametrosComuns.get("clie_id"));


						parameters.put("cliente", parametrosComuns.get("clie_id"));

						parameters.put("empr_nome", parametrosComuns.get("empr_nome"));
						parameters.put("gerente", parametrosComuns.get("gerente"));
						parameters.put("email", parametrosComuns.get("email"));
						parameters.put("telefone", parametrosComuns.get("fone"));

						parameters.put("conta_investimento", parametrosComuns.get("clie_id"));

						parameters.put("conta_deposito", parametrosComuns.get("clie_id"));
						parameters.put("tot_pg", new Integer(0));
						parameters.put("cab_rodape", ConnectBook.retornaCampoSQL("empr_book_rodape", "rc_empresas", " empr_id=" + Parametros.empr_id_SQL)); 
						parameters.put("cab_fantasia", ConnectBook.retornaCampoSQL("empr_book_fantasia", "rc_empresas", " empr_id=" + Parametros.empr_id_SQL));

						usr_pwd = parametrosComuns.get("usr_pwd").toString();
						cliente_nome =  parametrosComuns.get("clie_nome").toString();


						InputStream jpreload = ClassLoader.getSystemResourceAsStream("pci/jrv/workbook/preload.jasper");

						String workbookPath = "pci/jrv/workbook/Compilados/";

						List<JasperPrint> jasperPrinters = new ArrayList<JasperPrint>(); 

						//parametrosComuns = queriesUtil.obterParametrosComuns("1",Comm.formataSQL(pTaFec.getValueCod2(i,0),"IN"), Comm.formataSQL(pPnData1.getValue(), "D3"), Comm.formataSQL(pPnData2.getValue(), "D3"));


						Class c = Class.forName("CustomizadorGraficoBarraRoboto");
						Object obji = c.newInstance();
						c.getMethod("setConnection", new Class[] { Connection.class }).invoke(obji, new Object[] { ConnectBook.getConnection() });

						c = Class.forName("CustomizadorGraficoBarraCalibri");
						obji = c.newInstance();
						c.getMethod("setConnection", new Class[] { Connection.class }).invoke(obji, new Object[] { ConnectBook.getConnection() });

						c = Class.forName("CustomizadorGraficoBarraSimplesCRS");
						Object objii = c.newInstance();
						c.getMethod("setConnection", new Class[] { Connection.class }).invoke(objii, new Object[] { ConnectBook.getConnection() });

						Class g = Class.forName("grafico_overlaid_scriptlet_stackedarea_capital");
						Object objg = g.newInstance();
						g.getMethod("setConnection", new Class[] { Connection.class }).invoke(objg, new Object[] { ConnectBook.getConnection() });
						g.getMethod("setQuery",new Class[]{String.class}).invoke(objg, new Object[]{QueriesUtil.obterQueryCapitalAplicado(tabelasTemporarias.get("cli_sel").toString())});

						Class ScriptletUtil = Class.forName("ScriptletUtil");
						Object objScriptletUtil = ScriptletUtil.newInstance();
						ScriptletUtil.getMethod("setConnection", new Class[] { Connection.class }).invoke(objScriptletUtil, new Object[] { ConnectBook.getConnection() });
						ScriptletUtil.getMethod("setInicioRelatorio", new Class[] { boolean.class }).invoke(objScriptletUtil, new Object[] { true });

						parameters.put("REPORT_SCRIPTLET", ScriptletUtil.newInstance());



						int totalPaginas = 1;
						int numIndice = 2;


						StrSQL strSQLIndexes = new StrSQL();
						strSQLIndexes.add("create table #tmp_relatorioIndexes(");
						strSQLIndexes.add("		numPagina int");
						strSQLIndexes.add("		, tituloPagina varchar(80))");

						JasperFillManager.fillReport(jpreload, parameters, ConnectBook.getConnection());


						String rela_id = "1";

						StrSQL strSQLRelatorios = new StrSQL();
						strSQLRelatorios.add("DECLARE @dt_inicio VARCHAR(10)");
						strSQLRelatorios.add("	, @rela_id INT");
						strSQLRelatorios.add("	, @empr_id INT");
						strSQLRelatorios.add("	, @cart_id INT 	");
						strSQLRelatorios.add("	");
						strSQLRelatorios.add("SELECT @rela_id = " + rela_id);
						strSQLRelatorios.add("	 , @empr_id = 1");
						strSQLRelatorios.add("	 , @cart_id = " + cart_id);
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
						strSQLRelatorios.add("	   rp.repa_disclaimer, rp.repa_disclaimer,");
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
						strSQLRelatorios.add("  and rp.rela_id = @rela_id order by rp.repa_ordem");


						ResultSet rs = Connect.executeResultSQL(strSQLRelatorios);

						while (!Connect.eofSQL(rs)){
							relatorioAtual = Connect.leDadosSQL(rs, "repa_jasper");
							InputStream is = ClassLoader.getSystemResourceAsStream(workbookPath + relatorioAtual);
							parameters.put("titulo",Connect.leDadosSQL(rs, "repa_titulo_pagina"));
							parameters.put("textodisclaimer",Connect.leDadosSQL(rs, "repa_disclaimer"));
							JasperPrint rel = JasperFillManager.fillReport(is, parameters, ConnectBook.getConnection());
							if(rel.getPages().size()>0){
								strSQLIndexes.add("insert into #tmp_relatorioIndexes values ("+numIndice+",'"+Connect.leDadosSQL(rs, "repa_titulo_pagina")+"')");
							}
							numIndice = numIndice+rel.getPages().size();
							totalPaginas += rel.getPages().size();
							parameters.put("tot_pg", new Integer(totalPaginas)+1);//come�ar pag + 1 por causa merge comentarios
							Connect.nextSQL(rs);
							jasperPrinters.add(rel);

						}
						relatorioAtual = "";


						ScriptletUtil.getMethod("setInicioRelatorio", new Class[] { boolean.class }).invoke(objScriptletUtil, new Object[] { false });

						//�ndices
						boolean b = ConnectBook.executeSQL(strSQLIndexes);

						this.contadorRelatorios = 0;
						this.relatorios.setJasperArray(jasperPrinters.toArray(new JasperPrint[jasperPrinters.size()]));


						strSQLIndexes = new StrSQL();
						strSQLIndexes.add("drop table #tmp_relatorioIndexes");
						b = ConnectBook.executeSQL(strSQLIndexes);

						if(cbxImpressaoExportacao.getSelectedItem().toString().equals("Impress�o direta")){
							if(imprimir)
							{	
								if(pChBoxImpressaoGrafica.getSelected()){
									relatorios.ajustarParaEncadernacao();
								}
								for(int y=0;y<relatorios.getJasperArray().length;y++)
								{
									if(this.relatorios.getJasperArray()[y].getPages().size()!=0)
									{
										relatorios.getJasperArray()[y].setOrientation(OrientationEnum.LANDSCAPE);
										JRPrinterAWT.initPrinterJobFields(printJob);
										JRPrinterAWT.printPages(relatorios.getJasperArray()[y], 0, relatorios.getJasperArray()[y].getPages().size()-1, false,printJob,pras);
									}	
								}
							}
						}
						else{
							if(pChBoxImpressaoGrafica.getSelected()){
								relatorios.ajustarParaEncadernacao();
							}
							StrSQL strSQL = new StrSQL();
							strSQL.add("SELECT c.cart_nickname + '_' + CONVERT(VARCHAR(10), s.dt_fin, 112) + '_' + CONVERT(VARCHAR(10), GETDATE(), 112) + '-' + replace(Convert(varchar(8),GetDate(), 108),':','') AS nomeArquivo ");
							strSQL.add("FROM rc_carteiras c");
							strSQL.add("INNER JOIN " + tabelasTemporarias.get("cli_sel") + " s");
							strSQL.add("	ON s.empr_id = c.empr_id");
							strSQL.add("	AND s.cart_id = c.cart_id");

							ResultSet rs1 = ConnectBook.executeResultSQL(strSQL);
							String nomeArquivo = ConnectBook.leDadosSQL(rs1,"nomeArquivo");

							exportar(this.pTbArq.getText()+"\\"+nomeArquivo, cart_id);

						}

						if(pChCripSN.getValue().equals("s")){
							retClientes = Connect.retornaCampoSQL("cli.clie_cpf_cnpj","rc_clientes cli," + tabelasTemporarias.get("cli_sel") + " s, rc_carteiras c","c.empr_id = s.empr_id and c.cart_id = s.cart_id and c.clie_id = cli.clie_id");

							if(retClientes.equals("")){
								Comm.msgAviso("Clientes sem CPF Cadastrado, n�o � poss�vel criptografar o Relat�rio: "+retClientes  );
								retClientes = "";
							}   
						}

					}catch(Exception e){

						Comm.msgAviso("Ocorreu um erro ao gerar o relat�rio!");

						e.printStackTrace();
					}finally{
						this.setProgresso(incrementoRelatorios);
						QueriesUtil.proximo_Relatorio(tabelasTemporarias.get("cli_sel_tot").toString(), tabelasTemporarias.get("cli_sel").toString(), tabelasTemporarias.get("cli_selfilhas").toString(), tabelasTemporarias.get("cli_selbank").toString());
						this.relatorios = new Relatorios();

						if (criouIndice){
							StrSQL strSQLRelatorio = new StrSQL();
							strSQLRelatorio.add("drop table #tmp_relatorioIndexes");
							boolean b = ConnectBook.executeSQL(strSQLRelatorio);
							criouIndice = false;
						}
					}
				}
			}
			jProgressBarWorkbook.setValue(100);

			Comm.msgAviso("Lote de relat�rios gerado com sucesso!");

			if(!temCenarioEconomico){
				Comm.msgAviso("O arquivo de pdf com coment�rios econ�micos n�o foi inclu�do neste(s) relat�rio(s), pois n�o foi encontrado.\nArquivo informado: "+pdf1Path);
			}
			if(!temDsiclaimer){
				Comm.msgAviso("O arquivo de pdf com contato e disclaimer n�o foi inclu�do neste(s) relat�rio(s), pois n�o foi encontrado.\nArquivo informado: "+pdf2Path);
			}
			jProgressBarWorkbook.setVisible(false);
			pLbStatus.setVisible(false);
			this.pBuExecuta.setEnabled(true);
		}    
	}
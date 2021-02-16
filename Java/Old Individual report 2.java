	public void actionPerformed(ActionEvent e) {
		if ("Refresh".equalsIgnoreCase(e.getActionCommand())) {
			
			
			if (pPnDtFin.getValue().equals("")) {
			
				Comm.msgAviso("Data base � obrigat�ria!");
				return;
			}

			StrSQL strSQL = new StrSQL();
			strSQL.add(" select isnull((");
			strSQL.add("        select 1  ");
			strSQL.add("                   from rc_data_cart x");
			strSQL.add("                   where x.empr_id = " + pTsCart.getValueSQL("empr_id"));
			strSQL.add("                     and x.cart_id = " + pTsCart.getValueSQL("cart_id"));
			strSQL.add("                     and (x.data_status <> 'abe'");
			strSQL.add("                      or  dateadd(dd,-1,x.data_hoje) < " + pPnDtFin.getValueSQL());
			strSQL.add("                      or  dateadd(dd,0,x.data_lib)   < " + pPnDtFin.getValueSQL());
			strSQL.add("                )),0) nao_pode");

			ResultSet rule = ConnectBook.executeResultSQL(strSQL);

			String xpode1;

			xpode1 = (ConnectBook.leDadosSQL(rule, "nao_pode")).substring(0, 1);
			if (xpode1.equalsIgnoreCase("1") && (!Parametros.user_liberadata)) { // trocar
																					// 1==1
																					// por
																					// parametros
																					// usuario
																					// nao
																					// pode
																					// consultar
																					// data
																					// nao
																					// liberada
																					// //
				Comm.msgAviso("Data n�o liberada pelo Back Office  " + Comm.formataSQL(pPnDtFin.getValue(), "D3") + " ");

			} else {
				HashMap tabelasTemporarias = new HashMap();
				HashMap parametrosComuns;
				String relatorioAtual = "";
				try {           
	                pPanelConsulta.setCursor(Comm.getWaitCursor());
					tabelasTemporarias = QueriesUtil.obterTabelasTemporarias();
					ArrayList carteiras = new ArrayList();
					carteiras.add(pTsCart.getValueSQL(2).toString());
					String xempr = pTsCart.getValueSQL(1);
					String xcart = pTsCart.getValueSQL(2);
					String xdtini = PData.getPrimeiroDiaUtilMes(pPnDtFin.getValueSQL());
					String xdtiniD3 = Comm.formataSQL(PData.getPrimeiroDiaUtilMes(pPnDtFin.getValueSQL()), "D3");
					String xdtfin = Comm.formataSQL(pPnDtFin.getValue(), "D3");
					xdtfin = ConnectBook.retornaCampoSQL("max(hcar_dia)", "rc_hist_cart_dia", "empr_id = " + xempr + " and cart_id = " + xcart + " and hcar_dia  <= '" + xdtfin + "'");
					String ultimoDiaMEs = PData.getUltimoDiaUtilMes(pPnDtFin.getValueSQL());

					pPnDtFin.setValue(xdtfin);// MCT 12/05/2010 - a formata��o
												// anterior para sql invertia
												// dia com m�s no padr�o
												// americano de datas.

					if (QueriesUtil.carregar_TabelasTemporarias(xempr, xcart, tabelasTemporarias, carteiras, xdtiniD3, Comm.formataSQL(pPnDtFin.getValue(), "D3")) == true) {
						Map parameters = new HashMap();
						String retorno = null;
						String logo = ConnectBook.retornaCampoSQL("empr_logo", "rc_empresas", "empr_id = " + Parametros.empr_id_SQL);
						
						if (!logo.equalsIgnoreCase("")) {
							retorno = "pci/img/logo/book/" + logo;
						}
						parameters.put("BASE_DIR", "pci/jrv/workbookOffshore/Compilados");
						parameters.put("IMG_LOGO", "pci/img/logo/book/logo.png");
						parameters.put("IMG_TOPO_CAPA", "pci/img/logo/book/Legend.jpg");
						parameters.put("IMG_CANTO_CAPA", retorno);  // "pci/img/logo/book/gerval.jpg");
						parameters.put("IMG_CABECALHO", retorno);
						parameters.put("IMG_LOGO_CAB", "pci/img/logo/book/garin_cab.jpg");
						parameters.put("IMG_LOGO_CAB_FUNDO", "pci/img/logo/book/garin_logo_fundo.jpg");
						parameters.put("IMG_ANBIMA", "pci/img/logo/book/anbima.jpg");
						parameters.put("IMG_BRADESCO", "pci/img/logo/book/img_bradesco.jpg");
						parameters.put("IMG_CANTO", retorno);
						parameters.put("empr", new Integer(pTsCart.getValueSQL(1)));
						parameters.put("cart", new Integer(pTsCart.getValueSQL(2)));
						StrSQL strSQLCartTipoMoeda = new StrSQL();
						strSQLCartTipoMoeda.add("select c.cart_tipo,m.moed_nickname from rc_carteiras c,rc_moedas m where c.moed_id = m.moed_id and cart_id ="+ pTsCart.getValueSQL(2));
						ResultSet rss = Connect.executeResultSQL(strSQLCartTipoMoeda);
						String cart_tipo = Connect.leDadosSQL(rss, "cart_tipo");
						parameters.put("cart_tipo", cart_tipo);
						String moed_nickname = Connect.leDadosSQL(rss,"moed_nickname");
						StrSQL strSQLCartNickname = new StrSQL();
						strSQLCartNickname.add("select cart_nickname from rc_carteiras  where cart_id ="+ pTsCart.getValueSQL(2));
						ResultSet rsss = Connect.executeResultSQL(strSQLCartNickname);
						String cart_nickname = Connect.leDadosSQL(rsss, "cart_nickname");
						parameters.put("carteira", cart_nickname);
						parameters.put("moeda", moed_nickname);						
						parameters.put("datini", xdtini);
						parameters.put("dt_fin", pPnDtFin.getValue());
						parameters.put("idx1", new Integer(2));
						parameters.put("idx2", new Integer(1));
						parameters.put("cli_sel", tabelasTemporarias.get("cli_sel"));
						parameters.put("cli_sel_tot", tabelasTemporarias.get("cli_sel"));
						parameters.put("cli_selbank", tabelasTemporarias.get("cli_selbank"));
						
						parameters.put("data_inicial", QueriesUtil.obterDataInicial(tabelasTemporarias.get("cli_sel").toString()));
						parameters.put("cotDolar",
								QueriesUtil.obterdolar(tabelasTemporarias.get("cli_sel").toString()));
						parameters.put("cotEuro", QueriesUtil.obtereuro(tabelasTemporarias.get("cli_sel").toString()));
						
						parametrosComuns = QueriesUtil.obterParametrosComuns(pTsCart.getValueSQL(1), pTsCart.getValueSQL(2), Comm.formataSQL(PData.getPrimeiroDiaUtilMes(pPnDtFin.getValueSQL()), "D3"), Comm.formataSQL(pPnDtFin.getValue(), "D3"));

						parameters.put("periodo_ini", parametrosComuns.get("datini"));
						parameters.put("periodo_fim", parametrosComuns.get("dat_fin"));
						parameters.put("clie_id", parametrosComuns.get("clie_id"));
						
						//Jira-CA-156 - VA 03/02/20 - Alterado o nome por codigo na capa do book
						//parameters.put("cliente", parametrosComuns.get("clie_nome"));
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
		
						InputStream jpreload = ClassLoader.getSystemResourceAsStream("pci/jrv/workbookOffshore/preload.jasper");
						
						String workbookPath = "pci/jrv/workbookOffshore/Compilados/";
						
						InputStream is_Rel_Indice = ClassLoader.getSystemResourceAsStream(workbookPath + "Rel_Indice_Geral.jasper");	
						
						List<JasperPrint> jasperPrinters = new ArrayList<JasperPrint>(); 
						
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
						int totalPaginas = 0;
						int numIndice = 0;

		        		StrSQL strSQLIndexes = new StrSQL();
		        		strSQLIndexes.add("create table #tmp_relatorioIndexes(");
		        		strSQLIndexes.add("		numPagina int");
		        		strSQLIndexes.add("		, tituloPagina varchar(80))");					
		        		
		        		JasperFillManager.fillReport(jpreload, parameters, ConnectBook.getConnection());
		        		
						String rela_id = "2";
						
						StrSQL strSQLRelatorios = new StrSQL();
						strSQLRelatorios.add("DECLARE @dt_inicio VARCHAR(10)");
						strSQLRelatorios.add("	, @rela_id INT");
						strSQLRelatorios.add("	, @empr_id INT");
						strSQLRelatorios.add("	, @cart_id INT 	");
						strSQLRelatorios.add("	");
						strSQLRelatorios.add("SELECT @rela_id = " + rela_id);
						strSQLRelatorios.add("	 , @empr_id = " + pTsCart.getValueSQL(1));
						strSQLRelatorios.add("	 , @cart_id = " + pTsCart.getValueSQL(2));
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

						
						//final
						//JasperPrint Rel_Final = JasperFillManager.fillReport(is_Rel_Final, parameters, ConnectBook.getConnection());
						//jasperPrinters.add(Rel_Final);
						
						ScriptletUtil.getMethod("setInicioRelatorio", new Class[] { boolean.class }).invoke(objScriptletUtil, new Object[] { false });
						
						
						//cab
						//JasperPrint Rel_Cab = JasperFillManager.fillReport(is_Rel_Cab, parameters, ConnectBook.getConnection());
						//jasperPrinters.add(0, Rel_Cab);
						
						//�ndices
						boolean b = ConnectBook.executeSQL(strSQLIndexes);
						//parameters.put("titulo","�ndice");
						//JasperPrint Rel_Indice = JasperFillManager.fillReport(is_Rel_Indice, parameters, ConnectBook.getConnection());
						//asperPrinters.add(1, Rel_Indice);

						this.contadorRelatorios = 0;
						this.relatorios.setJasperArray(jasperPrinters.toArray(new JasperPrint[jasperPrinters.size()]));

						strSQLIndexes = new StrSQL();
		        		strSQLIndexes.add("drop table #tmp_relatorioIndexes");
		        		b = ConnectBook.executeSQL(strSQLIndexes);
		        		
						this.btnNextActionPerformed();
					}
				} catch (Exception ex) {
					ex.printStackTrace();
	                pPanelConsulta.setCursor(Comm.getDefaultCursor());
	                String msg = "Erro ao emitir relat�rio. Verificar o arquivo do log (abs_err.log) para maiores detalhes";
	                if (!relatorioAtual.equals(""))
	                	msg += "\nReport: " + relatorioAtual;
	                		
	                Comm.msgERRO(msg);
	                if(viewer != null){
	                    viewer.setVisible(false);
	                }
				} finally {
					if(!Parametros.debugQuery){
						QueriesUtil.deletar_TabelasTemporarias(tabelasTemporarias);
		    		}
	                pPanelConsulta.setCursor(Comm.getDefaultCursor());
				}
			}
		}
	}
	public void processa(){
		/*****Untouched code****/
		else if(msg==100){
			this.pLbStatus.setVisible(true);
			this.pBuExecuta.setEnabled(false);
			jProgressBarWorkbook.setVisible(true);			
			
			int progressoInicial = (int) (carteiras.size() * 0.01); // a ideia é iniciar a barra de progresso com algum valor quando o número de carteiras for muito grande, para não dar a sensação de que nada está sendo feito.
			progressoInicial = Math.max(progressoInicial, 1);
			jProgressBarWorkbook.setMaximum(carteiras.size()+progressoInicial);
			setProgresso(progressoInicial);
			
				for(String cartId: carteiras)
				{
					String emprId = "1";
					String dtIni = Comm.formataSQL(pPnData1.getValueSQL(),"D3");
					String dtFin = Comm.formataSQL(pPnData2.getValueSQL(),"D3");

					pLbStatus.setText("Gerando Relatorios Cart "+Connect.retornaCampoSQL("cart_nickname","rc_carteiras","empr_id = "+emprId+" and cart_id ="+cartId));

					GeraBook geraBook = new GeraBook(emprId, cartId, dtIni, dtFin);
					JasperPrint[] jasperPrints = geraBook.getBook();
					this.relatorios.setJasperArray(jasperPrints);
					
					printBook(printJob, imprimir, pras, cartId, emprId);
				}
			
			Comm.msgAviso("Lote de relatórios gerado com sucesso!");

			if(!temCenarioEconomico){
				Comm.msgAviso("O arquivo de pdf com comentários econômicos não foi incluído neste(s) relatório(s), pois não foi encontrado.\nArquivo informado: "+pdf1Path);
			}
			if(!temDsiclaimer){
				Comm.msgAviso("O arquivo de pdf com contato e disclaimer não foi incluído neste(s) relatório(s), pois não foi encontrado.\nArquivo informado: "+pdf2Path);
			}
			jProgressBarWorkbook.setVisible(false);
			pLbStatus.setVisible(false);
			this.pBuExecuta.setEnabled(true);
		}    
	}

	private void printBook(PrinterJob printJob, boolean imprimir, PrintRequestAttributeSet pras, String cartId,
			String emprId) {
		try{	
			if(cbxImpressaoExportacao.getSelectedItem().toString().equals("Impressão direta")){
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
			}else{
				if(pChBoxImpressaoGrafica.getSelected()){
					relatorios.ajustarParaEncadernacao();
				}
				StrSQL strSQL = new StrSQL();
				strSQL.add("SELECT cart_nickname + '_' + CONVERT(VARCHAR(10), "+pPnData2.getValueSQL()+", 112) + '_' + CONVERT(VARCHAR(10), GETDATE(), 112) + '-' + replace(Convert(varchar(8),GetDate(), 108),':','') AS nomeArquivo ");
				strSQL.add("FROM rc_carteiras");
				strSQL.add("where empr_id = "+ emprId);
				strSQL.add("	AND cart_id = "+ cartId);

				ResultSet rs1 = ConnectBook.executeResultSQL(strSQL);
				String nomeArquivo = ConnectBook.leDadosSQL(rs1,"nomeArquivo");

				exportar(this.pTbArq.getText()+"\\"+nomeArquivo, cartId);
			}

			if(pChCripSN.getValue().equals("s")){
				retClientes = Connect.retornaCampoSQL("cli.clie_cpf_cnpj","rc_clientes cli, rc_carteiras c","c.empr_id = "+emprId+" and c.cart_id = "+cartId+" and c.clie_id = cli.clie_id");

				if(retClientes.equals("")){
					Comm.msgAviso("Clientes sem CPF Cadastrado, não é possível criptografar o Relatório: "+retClientes  );
					retClientes = "";
				}   
			}
		}catch(Exception e){

			Comm.msgAviso("Ocorreu um erro ao gerar o relatório!");

			e.printStackTrace();
		}finally{
			this.setProgresso(1);
			this.relatorios = new Relatorios();
		}
	}
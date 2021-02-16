public void actionPerformed(ActionEvent e) {
		if ("Refresh".equalsIgnoreCase(e.getActionCommand())) {
			Comm.setWaitCursor();

			if (pPnDtFin.getValue().equals("")) {
				Comm.msgAviso("Data base é obrigatória!");
				return;
			}

			String emprId = pTsCart.getValueSQL(1);
			String cartId = pTsCart.getValueSQL(2);
			String dtFin = ConnectBook.retornaCampoSQL(
					"max(hcar_dia)",
					"rc_hist_cart_dia",
					"empr_id = " + emprId
					+ " and cart_id = " + cartId
					+ " and hcar_dia  <= " + pPnDtFin.getValueSQL());

			//Atualiza data final na tela
			pPnDtFin.setValue(dtFin);
			String dtIni = Comm.formataSQL(PData.getPrimeiroDiaUtilMes(pPnDtFin.getValueSQL()), "D3");

			if (isDataLiberada() && (!Parametros.user_liberadata)) {
				Comm.msgAviso("Data não liberada pelo Back Office  " + Comm.formataSQL(pPnDtFin.getValue(), "D3") + " ");
				return;
			}
			
			//Gera book
			GeraBook geraBook = new GeraBook(emprId, cartId, dtIni, dtFin);
			JasperPrint[] jasperPrints = geraBook.getBook();
			
			if(jasperPrints!=null) {
				paginaAtual = 0;
				relatorios.setJasperArray(jasperPrints);
				btnNextActionPerformed();
			}else {
				if(viewer != null){
					viewer.setVisible(false);
				}
			}
		}
		Comm.setDefaultCursor();

	}
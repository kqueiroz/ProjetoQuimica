package br.com.control;

import android.content.Intent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import br.com.model.DAO.Fachada;
import br.com.model.VO.Cadeia;
import br.com.model.VO.CadeiaAdapter;
import br.com.model.VO.DadosIUPAC;
import br.com.model.VO.Infixo;
import br.com.model.VO.Molecula;
import br.com.model.VO.Prefixo;
import br.com.model.VO.Sufixo;
import br.com.quimicapp.R;
import br.com.view.CadeiaActivity;
import br.com.view.CadeiaImagens;
import br.com.view.Composto_img;

public class ControleActivityCadeia implements View.OnClickListener {
    private CadeiaActivity context;
    private DadosIUPAC dadosIUPAC;
    private static final Cadeia cadeia = new Cadeia();

    public ControleActivityCadeia(CadeiaActivity context, Composto_img composto_img) throws Exception {
        this.context = context;
        Molecula molecula = new Molecula((int)composto_img.getComposto().getX(),(int)composto_img.getComposto().getY());
        this.getCadeia().getMoleculas().add(molecula);



        dadosIUPAC = new DadosIUPAC();
        String[] dados = preencherDadosPrefixos();

        for(int i=0; i<69;i++){
            dadosIUPAC.getPrefixos().add(new Prefixo(i+1, dados[i]));
        }
        dadosIUPAC.setInfixos(preencherDadosInfixos());
        dadosIUPAC.setSufixos(preencherDadosSufixos());

        //Inserir dados IUPAC(Prefixos, infixos e sufixos)
        Fachada.inserirDadosIUPAC(dadosIUPAC,this.context);

    }



    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.bt_gerar){
            int[] cont = new int[0];
            try {
              //  cont = this.getCadeia().gerarNomeclatura(this.getCadeia().getMoleculas().get(0),"");
            } catch (Exception e) {
                e.printStackTrace();
            }

            Cadeia cadeiaPrincipal =  new Cadeia();
            String nome = "Erro";
            String radical = "";
            StringBuilder stringBuilder = new StringBuilder(radical);

            ArrayList<String[]> radicais = new ArrayList<>();
            try {
//                nome = CadeiaAdapter.transformarString(
//                        this.getCadeia().gerarNomeclatura(this.getCadeia().getMoleculas().get(0), ""), context);



                List<Molecula> lista = new ArrayList<>();


                for (Molecula mol: cadeia.getMoleculas()
                     ) {
                    if(mol.getTipoLigUp().equals("tripla") ||
                            mol.getTipoLigRight().equals("tripla") ||
                            mol.getTipoLigDown().equals("tripla") ||
                            mol.getTipoLigLeft().equals("tripla")){
                        lista.add(mol);
                        break;
                    }
                }

                if(lista.isEmpty()){
                    for (Molecula mol: cadeia.getMoleculas()
                            ) {
                        if(mol.getTipoLigUp().equals("dupla") ||
                                mol.getTipoLigRight().equals("dupla") ||
                                mol.getTipoLigDown().equals("dupla") ||
                                mol.getTipoLigLeft().equals("dupla")){
                            lista.add(mol);
                            break;
                        }
                    }
                }

                if(!lista.isEmpty()) {
                    for (Molecula mole : lista
                            ) {
                        if (mole.getTipoLigUp().equals("tripla") || mole.getTipoLigRight().equals("tripla") ||
                                mole.getTipoLigDown().equals("tripla") || mole.getTipoLigLeft().equals("tripla")) {
                            cadeiaPrincipal.setMoleculas(cadeia.verificarCadeiaPrincipal(mole, "", true));
                            mole.setPrincipal(true);
                            for (Molecula m : cadeia.verificarCadeiaPrincipal(mole, "", true)) {
                                //System.out.println(m.getId());
                            }
                            break;
                        } else if (mole.getTipoLigUp().equals("dupla") || mole.getTipoLigRight().equals("dupla") ||
                                mole.getTipoLigDown().equals("dupla") || mole.getTipoLigLeft().equals("dupla")) {
                            cadeiaPrincipal.setMoleculas(cadeia.verificarCadeiaPrincipal(mole, "", true));
                            mole.setPrincipal(true);
                            for (Molecula m : cadeia.verificarCadeiaPrincipal(mole, "", true)) {
                                //System.out.println(m.getId());
                            }
                            break;
                        }
                    }
                }
                else {
                    Molecula moleculaInicial = cadeia.getMoleculas().get(0);
                    for (Molecula mo: cadeia.getMoleculas()
                         ) {
                        if(mo.getRamificacao()>2){
                            moleculaInicial = mo;
                            break;
                        }
                    }
                    moleculaInicial.setPrincipal(true);
                    cadeiaPrincipal.setMoleculas(cadeia.verificarCadeiaPrincipal(moleculaInicial, "", true));
                    for (Molecula m : cadeia.verificarCadeiaPrincipal(moleculaInicial, "", true)) {
                        //System.out.println(m.getId());
                    }
                }



                Cadeia cadeiaCompleta = new Cadeia();
                cadeiaCompleta.setMoleculas(cadeia.getMoleculas());

                for (Molecula m: cadeiaCompleta.getMoleculas()) {
                    Boolean existe= false;
                    for (Molecula mo: cadeiaPrincipal.getMoleculas()) {
                        if(m==mo) {
                            existe = true;
                            break;
                        }
                    }
                    if(!existe){
                        for (Molecula mol: cadeiaPrincipal.getMoleculas()
                                ) {
                            if(mol.getLigacaoSuperior()==m){
                                mol.setLigacaoSuperior(null);
                            }
                            else if(mol.getLigacaoDireita()==m){
                                mol.setLigacaoDireita(null);
                            }
                            else if(mol.getLigacaoInferior()==m){
                                mol.setLigacaoInferior(null);

                            }
                            else if(mol.getLigacaoEsquerda()==m){
                                mol.setLigacaoEsquerda(null);

                            }
                        }
                    }
                }

                for (Molecula molec: cadeiaPrincipal.getMoleculas()
                     ) {
                    if(molec.getPrincipal()){
                        //System.out.println(cadeiaPrincipal.verificarRamificacaoMaior(cadeiaPrincipal.contarCarbonosCadeiaPrincipal("", molec))[1]);

                        cadeiaPrincipal.numerarCadeiaPrincipal(molec,"",
                                cadeiaPrincipal.verificarRamificacaoMaior(cadeiaPrincipal.contarCarbonosCadeiaPrincipal("", molec)),true, "menor");
                    }
                }

                for (Molecula m: cadeiaPrincipal.getMoleculas()
                     ) {
                   //System.out.println("id= "+m.getId()+" n: "+m.getNumeracaoNaCadeia());
                }

                //Correto abaixo
                voltarCadeiaCopia(cadeiaPrincipal);

                for (Molecula m: cadeiaCompleta.getMoleculas()) {
                    Boolean existe= false;
                    for (Molecula mo: cadeiaPrincipal.getMoleculas()) {
                        if(m==mo) {
                            existe = true;
                            break;
                        }
                    }
                    if(!existe){
                        for (Molecula mol: cadeiaPrincipal.getMoleculas()
                             ) {
                            String nomeRad;
                            Boolean existeRad = false;
                            if(mol.getLigacaoSuperior()==m){
                                mol.setLigacaoSuperior(null);
                                nomeRad = CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m, "down"), context);

                                for (String[] listaRad: radicais
                                     ) {
                                    if(nomeRad.equals(listaRad[1])){
                                        String tmp = listaRad[0];
                                        listaRad[0] = tmp+", "+mol.getNumeracaoNaCadeia();
                                        existeRad = true;
                                        break;
                                    }
                                }

                               if(!existeRad){
                                    String[] rads = new String[2];
                                    rads[0] = String.valueOf(mol.getNumeracaoNaCadeia());
                                    rads[1] = nomeRad;
                                    radicais.add(rads);
                               }


                                //stringBuilder.append(mol.getNumeracaoNaCadeia() + "- " + CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m, "down"), context) + ", ");

                            }
                            else if(mol.getLigacaoDireita()==m){
                                mol.setLigacaoDireita(null);
                                nomeRad = CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m, "left"), context);

                                for (String[] listaRad: radicais
                                        ) {
                                    if(nomeRad.equals(listaRad[1])){
                                        String tmp = listaRad[0];
                                        listaRad[0] = tmp+", "+mol.getNumeracaoNaCadeia();
                                        existeRad = true;
                                        break;
                                    }
                                }

                                if(!existeRad){
                                    String[] rads = new String[2];
                                    rads[0] = String.valueOf(mol.getNumeracaoNaCadeia());
                                    rads[1] = nomeRad;
                                    radicais.add(rads);
                                }
                                //stringBuilder.append(mol.getNumeracaoNaCadeia()+"- "+CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m,"left"), context)+", ");

                            }
                            else if(mol.getLigacaoInferior()==m){
                                mol.setLigacaoInferior(null);
                                nomeRad = CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m, "up"), context);

                                for (String[] listaRad: radicais
                                        ) {
                                    if(nomeRad.equals(listaRad[1])){
                                        String tmp = listaRad[0];
                                        listaRad[0] = tmp+", "+mol.getNumeracaoNaCadeia();
                                        existeRad = true;
                                        break;
                                    }
                                }

                                if(!existeRad){
                                    String[] rads = new String[2];
                                    rads[0] = String.valueOf(mol.getNumeracaoNaCadeia());
                                    rads[1] = nomeRad;
                                    radicais.add(rads);
                                }
                                //stringBuilder.append(mol.getNumeracaoNaCadeia()+"- "+CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m,"up"), context)+", ");

                            }
                            else if(mol.getLigacaoEsquerda()==m){
                                mol.setLigacaoEsquerda(null);
                                nomeRad = CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m, "right"), context);

                                for (String[] listaRad: radicais
                                        ) {
                                    if(nomeRad.equals(listaRad[1])){
                                        String tmp = listaRad[0];
                                        listaRad[0] = tmp+", "+mol.getNumeracaoNaCadeia();
                                        existeRad = true;
                                        break;
                                    }
                                }

                                if(!existeRad){
                                    String[] rads = new String[2];
                                    rads[0] = String.valueOf(mol.getNumeracaoNaCadeia());
                                    rads[1] = nomeRad;
                                    radicais.add(rads);
                                }
                                //stringBuilder.append(mol.getNumeracaoNaCadeia()+"- "+CadeiaAdapter.transformarStringRadical(cadeia.gerarNomeclatura(m,"right"), context)+", ");

                            }
                        }
                    }
                }


                //System.out.println(stringBuilder);

                nome = CadeiaAdapter.transformarString(
                        this.getCadeia().gerarNomeclatura(cadeiaPrincipal.getMoleculas().get(0), ""), context);


            } catch (Exception e) {
                e.printStackTrace();
            }

            String palavra = stringBuilder.toString();

            String[] separaPorVirgula = palavra.split(", ");

            ArrayList<String[]> componentes = new ArrayList<>();


            for (int i = 0; i < separaPorVirgula.length; i++) {
                componentes.add(separaPorVirgula[i].split("- "));
            }

            ArrayList<String> stgs = new ArrayList();
            for (String[] lista : componentes) {
                if (lista.length > 1) {
                    System.out.println(lista[0] + "-" + lista[1]);


                } else {

                }
            }

            for (String[] rads: radicais
                 ) {
                stringBuilder.append(rads[0]+"- "+rads[1]+", ");
            }

            context.alertNomenclatura(stringBuilder+nome);


            //Voltar a copia

            voltarCadeiaCopia(cadeiaPrincipal);

        }

        if(v.getId()== R.id.bt_limpar){
            try {
                this.getCadeia().getMoleculas().clear();
            } catch (Exception e) {
                e.printStackTrace();
            }
            CadeiaImagens.getCadeiaImagens().getCompostosImagens().clear();

            Molecula.zerarContador();
            Composto_img.zerarContador();

            Intent i = new Intent(context, CadeiaActivity.class);
            context.startActivity(i);
            context.finish();
        }

        if(v.getId()==R.id.img_close || v.getId()==R.id.bt_ok){
            context.getAlerta().dismiss();
        }
    }

    public void voltarCadeiaCopia(Cadeia cadeiaPrincipal){
        try {
            Cadeia cadeiaCompleta = new Cadeia();
            cadeiaCompleta.setMoleculas(cadeia.getMoleculas());

            for (Molecula m: cadeiaCompleta.getMoleculas()) {
                for (Molecula mo: cadeiaPrincipal.getMoleculas()) {
                    if(m!=mo) {
                        if(m.getLigacaoSuperior()==mo){
                            mo.setLigacaoInferior(m);
                        }
                        else if(m.getLigacaoDireita()==mo){
                            mo.setLigacaoEsquerda(m);
                        }
                        else if(m.getLigacaoInferior()==mo){
                            mo.setLigacaoSuperior(m);
                        }
                        else if(m.getLigacaoEsquerda()==mo){
                            mo.setLigacaoDireita(m);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Sufixo> preencherDadosSufixos(){
        ArrayList<Sufixo> sufixos = new ArrayList<>();

        sufixos.add(new Sufixo("hidrocarboneto", "o"));
        sufixos.add(new Sufixo("alcool", "ol"));
        sufixos.add(new Sufixo("aldeido", "al"));
        sufixos.add(new Sufixo("cetona", "ona"));
        sufixos.add(new Sufixo("ácido carboxilico", "óico"));
        return sufixos;
    }

    public ArrayList<Infixo> preencherDadosInfixos(){
        ArrayList<Infixo> infixos =  new ArrayList();

        infixos.add(new Infixo(1,"","an"));
        infixos.add(new Infixo(1,"","en"));
        infixos.add(new Infixo(1,"","in"));

        infixos.add(new Infixo(2,"","an"));
        infixos.add(new Infixo(2,"di","en"));
        infixos.add(new Infixo(2,"d","in"));

        infixos.add(new Infixo(3,"","an"));
        infixos.add(new Infixo(3,"tri","en"));
        infixos.add(new Infixo(3,"tr","in"));

        infixos.add(new Infixo(4,"","an"));
        infixos.add(new Infixo(4,"tetra","en"));
        infixos.add(new Infixo(4,"tetra","in"));

        infixos.add(new Infixo(5,"","an"));
        infixos.add(new Infixo(5,"penta","en"));
        infixos.add(new Infixo(5,"penta","in"));

        infixos.add(new Infixo(6,"","an"));
        infixos.add(new Infixo(6,"hexa","en"));
        infixos.add(new Infixo(6,"hexa","in"));

        infixos.add(new Infixo(7,"","an"));
        infixos.add(new Infixo(7,"hepta","en"));
        infixos.add(new Infixo(7,"hepta","in"));

        infixos.add(new Infixo(8,"","an"));
        infixos.add(new Infixo(8,"octa","en"));
        infixos.add(new Infixo(8,"octa","in"));

        infixos.add(new Infixo(9,"","an"));
        infixos.add(new Infixo(9,"nona","en"));
        infixos.add(new Infixo(9,"nona","in"));


        return infixos;

    };


    public String[] preencherDadosPrefixos(){
        String[] palavras = new String[100];

        //Prefixos
        palavras[0]="met";
        palavras[1]="et";
        palavras[2]="prop";
        palavras[3]="but";
        palavras[4]="pent";
        palavras[5]="hex";
        palavras[6]="hept";
        palavras[7]="oct";
        palavras[8]="non";
        palavras[9]="dec";
        palavras[10]="undec";
        palavras[11]="dodec";
        palavras[12]="tridec";
        palavras[13]="tetradec";
        palavras[14]="pentadec";
        palavras[15]="hexadec";
        palavras[16]="heptadec";
        palavras[17]="octadec";
        palavras[18]="nonadec";
        palavras[19]="eicos";
        palavras[20]="heneicos";
        palavras[21]="docos";
        palavras[22]="tricos";
        palavras[23]="tetracos";
        palavras[24]="pentacos";
        palavras[25]="hexacos";
        palavras[26]="heptacos";
        palavras[27]="octacos";
        palavras[28]="nonacos";
        palavras[29]="triacont";
        palavras[30]="hentriacont";
        palavras[31]="dotriacont";
        palavras[32]="tritriacont";
        palavras[33]="tetratriacont";
        palavras[34]="pentatriacont";
        palavras[35]="hexatriacont";
        palavras[36]="heptriacont";
        palavras[37]="octatriacont";
        palavras[38]="nonatriacont";
        palavras[39]="tetracont";
        palavras[40]="hentetracont";
        palavras[41]="dotetracont";
        palavras[42]="tritetracont";
        palavras[43]="tetratetracont";
        palavras[44]="pentatetracont";
        palavras[45]="hexatetracont";
        palavras[46]="heptetracont";
        palavras[47]="octatetracont";
        palavras[48]="nonatetracont";
        palavras[49]="pentacont";
        palavras[50]="henpentacont";
        palavras[51]="dopentacont";
        palavras[52]="tripentacont";
        palavras[53]="tetrapentacont";
        palavras[54]="pentapentacont";
        palavras[55]="hexapentacont";
        palavras[56]="heptpentacont";
        palavras[57]="octapentacont";
        palavras[58]="nonapentacont";
        palavras[59]="hexacont";
        palavras[60]="henhexacont";
        palavras[61]="dohexacont";
        palavras[62]="trihexacont";
        palavras[63]="tetrahexacont";
        palavras[64]="pentahexacont";
        palavras[65]="hexahexacont";
        palavras[66]="hepthexacont";
        palavras[67]="octahexacont";
        palavras[68]="nonahexacont";

        return palavras;
    }

    public static Cadeia getCadeia() {
        return cadeia;
    }
}

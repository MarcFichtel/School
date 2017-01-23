/*
 * VendingMachineParser.cs
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

using System.IO;

using PerCederberg.Grammatica.Runtime;

namespace Frontend1.Parser {

    /**
     * <remarks>A token stream parser.</remarks>
     */
    public class VendingMachineParser : RecursiveDescentParser {

        /**
         * <summary>An enumeration with the generated production node
         * identity constants.</summary>
         */
        private enum SynteticPatterns {
            SUBPRODUCTION_1 = 3001,
            SUBPRODUCTION_2 = 3002,
            SUBPRODUCTION_3 = 3003,
            SUBPRODUCTION_4 = 3004,
            SUBPRODUCTION_5 = 3005
        }

        /**
         * <summary>Creates a new parser with a default analyzer.</summary>
         *
         * <param name='input'>the input stream to read from</param>
         *
         * <exception cref='ParserCreationException'>if the parser
         * couldn't be initialized correctly</exception>
         */
        public VendingMachineParser(TextReader input)
            : base(input) {

            CreatePatterns();
        }

        /**
         * <summary>Creates a new parser.</summary>
         *
         * <param name='input'>the input stream to read from</param>
         *
         * <param name='analyzer'>the analyzer to parse with</param>
         *
         * <exception cref='ParserCreationException'>if the parser
         * couldn't be initialized correctly</exception>
         */
        public VendingMachineParser(TextReader input, VendingMachineAnalyzer analyzer)
            : base(input, analyzer) {

            CreatePatterns();
        }

        /**
         * <summary>Creates a new tokenizer for this parser. Can be overridden
         * by a subclass to provide a custom implementation.</summary>
         *
         * <param name='input'>the input stream to read from</param>
         *
         * <returns>the tokenizer created</returns>
         *
         * <exception cref='ParserCreationException'>if the tokenizer
         * couldn't be initialized correctly</exception>
         */
        protected override Tokenizer NewTokenizer(TextReader input) {
            return new VendingMachineTokenizer(input);
        }

        /**
         * <summary>Initializes the parser by creating all the production
         * patterns.</summary>
         *
         * <exception cref='ParserCreationException'>if the parser
         * couldn't be initialized correctly</exception>
         */
        private void CreatePatterns() {
            ProductionPattern             pattern;
            ProductionPatternAlternative  alt;

            pattern = new ProductionPattern((int) VendingMachineConstants.SCRIPT,
                                            "Script");
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.COMMAND, 1, -1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.COMMAND,
                                            "Command");
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.CREATE, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.CONFIGURE, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.COIN_LOAD, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.POP_LOAD, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.UNLOAD, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.EXTRACT, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.INSERT, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.PRESS, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.CHECK_DELIVERY, 1, 1);
            pattern.AddAlternative(alt);
            alt = new ProductionPatternAlternative();
            alt.AddProduction((int) VendingMachineConstants.CHECK_TEARDOWN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.CREATE,
                                            "Create");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.CREATE_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddProduction((int) SynteticPatterns.SUBPRODUCTION_1, 0, -1);
            alt.AddToken((int) VendingMachineConstants.SEMI_COLON, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.CONFIGURE,
                                            "Configure");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.CONFIGURE_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.VMSELECTION, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.POP_STRING, 1, 1);
            alt.AddToken((int) VendingMachineConstants.COMMA, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddProduction((int) SynteticPatterns.SUBPRODUCTION_2, 0, -1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.COIN_LOAD,
                                            "CoinLoad");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.COIN_LOAD_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.VMSELECTION, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.SEMI_COLON, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.COMMA, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.POP_LOAD,
                                            "PopLoad");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.POP_LOAD_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.VMSELECTION, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.SEMI_COLON, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.POP_STRING, 1, 1);
            alt.AddToken((int) VendingMachineConstants.COMMA, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.UNLOAD,
                                            "Unload");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.UNLOAD_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.VMSELECTION, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.EXTRACT,
                                            "Extract");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.EXTRACT_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.VMSELECTION, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.INSERT,
                                            "Insert");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.INSERT_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.VMSELECTION, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.PRESS,
                                            "Press");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.PRESS_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.VMSELECTION, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.CHECK_DELIVERY,
                                            "CheckDelivery");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.CHECK_DELIVERY_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddProduction((int) SynteticPatterns.SUBPRODUCTION_3, 0, -1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.CHECK_TEARDOWN,
                                            "CheckTeardown");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.CHECK_TEARDOWN_COMMAND, 1, 1);
            alt.AddToken((int) VendingMachineConstants.LEFT_PAREN, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.SEMI_COLON, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddProduction((int) SynteticPatterns.SUBPRODUCTION_5, 0, -1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.VMSELECTION,
                                            "VMSelection");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.LEFT_SQUARE_PAREN, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            alt.AddToken((int) VendingMachineConstants.RIGHT_SQUARE_PAREN, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) VendingMachineConstants.POP_STRING,
                                            "PopString");
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.QUOTATION_MARK, 1, 1);
            alt.AddToken((int) VendingMachineConstants.STRING, 1, 1);
            alt.AddToken((int) VendingMachineConstants.QUOTATION_MARK, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) SynteticPatterns.SUBPRODUCTION_1,
                                            "Subproduction1");
            pattern.Synthetic = true;
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.COMMA, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) SynteticPatterns.SUBPRODUCTION_2,
                                            "Subproduction2");
            pattern.Synthetic = true;
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.SEMI_COLON, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.POP_STRING, 1, 1);
            alt.AddToken((int) VendingMachineConstants.COMMA, 1, 1);
            alt.AddToken((int) VendingMachineConstants.NUMBER, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) SynteticPatterns.SUBPRODUCTION_3,
                                            "Subproduction3");
            pattern.Synthetic = true;
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.COMMA, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.POP_STRING, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) SynteticPatterns.SUBPRODUCTION_4,
                                            "Subproduction4");
            pattern.Synthetic = true;
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.COMMA, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.POP_STRING, 1, 1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);

            pattern = new ProductionPattern((int) SynteticPatterns.SUBPRODUCTION_5,
                                            "Subproduction5");
            pattern.Synthetic = true;
            alt = new ProductionPatternAlternative();
            alt.AddToken((int) VendingMachineConstants.SEMI_COLON, 1, 1);
            alt.AddProduction((int) VendingMachineConstants.POP_STRING, 1, 1);
            alt.AddProduction((int) SynteticPatterns.SUBPRODUCTION_4, 0, -1);
            pattern.AddAlternative(alt);
            AddPattern(pattern);
        }
    }
}

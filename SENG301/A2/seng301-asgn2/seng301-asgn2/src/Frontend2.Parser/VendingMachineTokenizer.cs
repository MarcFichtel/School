/*
 * VendingMachineTokenizer.cs
 *
 * THIS FILE HAS BEEN GENERATED AUTOMATICALLY. DO NOT EDIT!
 */

using System.IO;

using PerCederberg.Grammatica.Runtime;

namespace Frontend2.Parser {

    /**
     * <remarks>A character stream tokenizer.</remarks>
     */
    public class VendingMachineTokenizer : Tokenizer {

        /**
         * <summary>Creates a new tokenizer for the specified input
         * stream.</summary>
         *
         * <param name='input'>the input stream to read</param>
         *
         * <exception cref='ParserCreationException'>if the tokenizer
         * couldn't be initialized correctly</exception>
         */
        public VendingMachineTokenizer(TextReader input)
            : base(input, false) {

            CreatePatterns();
        }

        /**
         * <summary>Initializes the tokenizer by creating all the token
         * patterns.</summary>
         *
         * <exception cref='ParserCreationException'>if the tokenizer
         * couldn't be initialized correctly</exception>
         */
        private void CreatePatterns() {
            TokenPattern  pattern;

            pattern = new TokenPattern((int) VendingMachineConstants.SEMI_COLON,
                                       "SEMI_COLON",
                                       TokenPattern.PatternType.STRING,
                                       ";");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.COMMA,
                                       "COMMA",
                                       TokenPattern.PatternType.STRING,
                                       ",");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.LEFT_PAREN,
                                       "LEFT_PAREN",
                                       TokenPattern.PatternType.STRING,
                                       "(");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.RIGHT_PAREN,
                                       "RIGHT_PAREN",
                                       TokenPattern.PatternType.STRING,
                                       ")");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.LEFT_SQUARE_PAREN,
                                       "LEFT_SQUARE_PAREN",
                                       TokenPattern.PatternType.STRING,
                                       "[");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.RIGHT_SQUARE_PAREN,
                                       "RIGHT_SQUARE_PAREN",
                                       TokenPattern.PatternType.STRING,
                                       "]");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.QUOTATION_MARK,
                                       "QUOTATION_MARK",
                                       TokenPattern.PatternType.REGEXP,
                                       "\\\"");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.CREATE_COMMAND,
                                       "CREATE_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "CREATE");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.CONFIGURE_COMMAND,
                                       "CONFIGURE_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "CONFIGURE");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.COIN_LOAD_COMMAND,
                                       "COIN_LOAD_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "COIN_LOAD");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.POP_LOAD_COMMAND,
                                       "POP_LOAD_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "POP_LOAD");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.UNLOAD_COMMAND,
                                       "UNLOAD_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "UNLOAD");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.EXTRACT_COMMAND,
                                       "EXTRACT_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "EXTRACT");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.INSERT_COMMAND,
                                       "INSERT_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "INSERT");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.PRESS_COMMAND,
                                       "PRESS_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "PRESS");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.CHECK_DELIVERY_COMMAND,
                                       "CHECK_DELIVERY_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "CHECK_DELIVERY");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.CHECK_TEARDOWN_COMMAND,
                                       "CHECK_TEARDOWN_COMMAND",
                                       TokenPattern.PatternType.STRING,
                                       "CHECK_TEARDOWN");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.NUMBER,
                                       "NUMBER",
                                       TokenPattern.PatternType.REGEXP,
                                       "[0-9]+");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.STRING,
                                       "STRING",
                                       TokenPattern.PatternType.REGEXP,
                                       "[A-Za-z]+");
            AddPattern(pattern);

            pattern = new TokenPattern((int) VendingMachineConstants.WHITESPACE,
                                       "WHITESPACE",
                                       TokenPattern.PatternType.REGEXP,
                                       "[ \\t\\n\\r]+");
            pattern.Ignore = true;
            AddPattern(pattern);
        }
    }
}

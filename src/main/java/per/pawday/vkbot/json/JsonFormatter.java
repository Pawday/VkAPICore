package per.pawday.vkbot.json;


import java.io.*;
import java.nio.charset.StandardCharsets;


public class JsonFormatter
{

    private static StringBuilder formatToBuilder(InputStream stream, String separator) throws IOException
    {


        InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8);


        StringBuilder builder = new StringBuilder();


        int indent = 0;

        boolean inString = false;

        char c;
        int by;

        while ((by = reader.read()) != -1)
        {
            c = (char) by;

            switch (c)
            {

                case '{':
                case '[':
                    if (! inString)
                    {
                        if (builder.length() != 0 )
                        {
                            builder.append('\n');
                            for (int i = 0; i < indent; i++)
                            {
                                builder.append(separator);
                            }
                        }

                        builder.append(c).append('\n');
                        indent++;
                        for (int i = 0; i < indent; i++)
                        {
                            builder.append(separator);
                        }
                    } else builder.append(c);
                    break;

                case '}':
                case ']':
                    indent--;
                    builder.append('\n');
                    for (int i = 0; i < indent; i++)
                    {
                        builder.append(separator);
                    }
                    builder.append(c);
                    break;

                case '"':
                    if (! inString)
                    {
                        inString = true;
                    }
                    else
                    {
                        inString = false;
                    }

                    builder.append('"');
                    break;


                case ',':

                    if (! inString)
                    {
                        builder.append(',').append('\n');
                        for (int i = 0; i < indent; i++)
                        {
                            builder.append(separator);
                        }
                    } else builder.append(c);

                    break;


                case ':':
                    if (! inString)
                    {
                        builder.append(' ').append(':').append(' ');
                    } else builder.append(c);
                    break;

                case '\t':

                case ' ':
                    if (inString)
                    {
                        builder.append(' ');
                    }
                    break;

                case '\n':
                case '\r':

                    break;



                default:
                    builder.append(c);

            }


        }

        return builder;
    }


    public static StringBuilder formatToStringBuilder(InputStream stream,String separator) throws IOException
    {
        return formatToBuilder(stream,separator);
    }

    public static StringBuilder formatToStringBuilder(String string,String separator) throws IOException
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return formatToBuilder(new ByteInputStream(bytes),separator);
    }

    public static StringBuilder formatToStringBuilder(Reader reader,String separator) throws IOException
    {
        return formatToBuilder(new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return reader.read();
            }
        }, separator);
    }

    public static String formatToString(InputStream stream,String separator) throws IOException
    {
        return formatToBuilder(stream,separator).toString();
    }

    public static String formatToString(String string,String separator) throws IOException
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        return formatToBuilder(new ByteInputStream(bytes),separator).toString();
    }

    public static String formatToString(Reader reader,String separator) throws IOException
    {
        return formatToBuilder(new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return reader.read();
            }
        },separator).toString();
    }



    public static InputStream formatToInputStream(InputStream stream,String separator) throws IOException
    {
        StringBuilder result =  formatToBuilder(stream,separator);
        return new CharsInputStream(result);
    }

    public static InputStream formatToInputStream(String string,String separator) throws IOException
    {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);
        StringBuilder result = formatToBuilder(new ByteInputStream(bytes),separator);
        return new CharsInputStream(result);
    }

    public static InputStream formatToInputStream(Reader reader,String separator) throws IOException
    {
        StringBuilder result = formatToBuilder(new InputStream()
        {
            @Override
            public int read() throws IOException
            {
                return reader.read();
            }
        },separator);
        return new CharsInputStream(result);
    }


    private static class ByteInputStream extends InputStream
    {
        private byte[] bytes;
        private int iter = 0;

        public ByteInputStream(byte[] bytes)
        {
            this.bytes = bytes;
        }

        @Override
        public int read() throws IOException
        {
            if (iter == 0)
            {
                iter++;
                return bytes[0];
            }
            else
            {
                if (iter == bytes.length)
                {
                    return -1;
                }
                else
                {
                    iter++;
                    return bytes[iter - 1];
                }
            }
        }
    }


    private static class CharsInputStream extends InputStream
    {
        private StringBuilder builder;
        private int length;
        private int pos;

        private CharsInputStream(StringBuilder builder)
        {
            this.builder = builder;
            this.length = builder.length();
            this.pos = 0;
        }


        @Override
        public int read()
        {

            if ( this.pos == this.length )
            {
                return -1;
            }

            this.pos++;


            return this.builder.charAt(this.pos - 1);
        }
    }


}

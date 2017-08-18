package org.broadinstitute.hellbender.utils.config;

import org.aeonbits.owner.Accessible;
import org.aeonbits.owner.Mutable;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.LoadType;
import org.aeonbits.owner.Config.Sources;

import java.util.List;

/**
 * Configuration file for GATK options.
 */
@LoadPolicy(LoadType.MERGE)
@Sources({
        "file:${" + GATKConfig.CONFIG_FILE_VARIABLE_NAME + "}",                         // Variable for file loading
        "file:GATKConfig.properties",                                                   // Default path
        "classpath:org/broadinstitute/hellbender/utils/config/GATKConfig.properties"    // Class path
})
public interface GATKConfig extends Mutable, Accessible {

    // =================================================================================================================
    // =================================================================================================================
    // Meta Options:
    // =================================================================================================================
    // =================================================================================================================

    /**
     * Name of the configuration file variable to be used in the {@link Sources} annotation for {@link GATKConfig}
     * as a place to find the configuration file corresponding to this interface.
     */
    String CONFIG_FILE_VARIABLE_NAME = "GATKConfig.pathToGatkConfig";

    // =================================================================================================================
    // =================================================================================================================
    // System Options:
    // =================================================================================================================
    // =================================================================================================================

    // ----------------------------------------------------------
    // Miscellaneous Options:
    // ----------------------------------------------------------

    @SystemProperty
    @Key("gatk_stacktrace_on_user_exception")
    @DefaultValue("true")
    boolean gatk_stacktrace_on_user_exception();

    // ----------------------------------------------------------
    // HTSJDK Options:
    // ----------------------------------------------------------

    @SystemProperty
    @Key("samjdk.use_async_io_read_samtools")
    @ConverterClass(CustomBooleanConverter.class)
    @DefaultValue("false")
    Boolean samjdk_use_async_io_read_samtools();

    @SystemProperty
    @Key("samjdk.use_async_io_write_samtools")
    @DefaultValue("true")
    boolean samjdk_use_async_io_write_samtools();

    @SystemProperty
    @Key("samjdk.use_async_io_write_tribble")
    @DefaultValue("false")
    boolean samjdk_use_async_io_write_tribble();

    @SystemProperty
    @Key("samjdk.compression_level")
    @DefaultValue("1")
    int samjdk_compression_level();

    @SystemProperty
    @Key("snappy.disable")
    @DefaultValue("true")
    boolean snappy_disable();

    // ----------------------------------------------------------
    // Spark Options:
    // ----------------------------------------------------------

    @SystemProperty
    @Key("spark.kryoserializer.buffer.max")
    @DefaultValue("512m")
    String spark_kryoserializer_buffer_max();

    @SystemProperty
    @Key("spark.driver.maxResultSize")
    @DefaultValue("0")
    int spark_driver_maxResultSize();

    @SystemProperty
    @Key("spark.driver.userClassPathFirst")
    @DefaultValue("true")
    boolean spark_driver_userClassPathFirst();

    @SystemProperty
    @Key("spark.io.compression.codec")
    @DefaultValue("lzf")
    String spark_io_compression_codec();

    @SystemProperty
    @Key("spark.yarn.executor.memoryOverhead")
    @DefaultValue("600")
    int spark_yarn_executor_memoryOverhead();

    @SystemProperty
    @Key("spark.driver.extraJavaOptions")
    @DefaultValue("")
    String spark_driver_extraJavaOptions();

    @SystemProperty
    @Key("spark.executor.extraJavaOptions")
    @DefaultValue("")
    String spark_executor_extraJavaOptions();

    // =================================================================================================================
    // =================================================================================================================
    // GATK  Options:
    // =================================================================================================================
    // =================================================================================================================
    
    // ----------------------------------------------------------
    // Miscellaneous Options:
    // ----------------------------------------------------------

    @DefaultValue("htsjdk.variant,htsjdk.tribble,org.broadinstitute.hellbender.utils.codecs")
    List<String> codec_packages();

    // ----------------------------------------------------------
    // GATKTool Options:
    // ----------------------------------------------------------

    @DefaultValue("40")
    int cloudPrefetchBuffer();

    @DefaultValue("-1")
    int cloudIndexPrefetchBuffer();

    @DefaultValue("20")
    int gcsMaxNumRetries();

    @DefaultValue("true")
    boolean createOutputBamIndex();
}
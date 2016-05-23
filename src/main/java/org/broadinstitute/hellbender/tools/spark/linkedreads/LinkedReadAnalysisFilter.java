package org.broadinstitute.hellbender.tools.spark.linkedreads;

import htsjdk.samtools.CigarElement;
import htsjdk.samtools.CigarOperator;
import htsjdk.samtools.SAMFileHeader;
import htsjdk.samtools.filter.OverclippedReadFilter;
import org.broadinstitute.hellbender.engine.filters.ReadFilter;
import org.broadinstitute.hellbender.engine.filters.ReadFilterLibrary;
import org.broadinstitute.hellbender.utils.read.GATKRead;

public class LinkedReadAnalysisFilter implements ReadFilter {
    private static final long serialVersionUID = 1l;

    ReadFilter filter;

    public LinkedReadAnalysisFilter(final double minEntropy) {
        this.filter = ReadFilterLibrary.MAPPED
                .and(ReadFilterLibrary.PASSES_VENDOR_QUALITY_CHECK)
                .and(ReadFilterLibrary.NOT_DUPLICATE)
                .and(ReadFilterLibrary.PRIMARY_ALIGNMENT)
                .and(read -> !read.isSupplementaryAlignment())
                .and(read -> read.hasAttribute("BX"))
                .and(read -> ! filterOut(read, 32, false));
        if (minEntropy > 0) {
            this.filter = this.filter.and(new ReadEntropyFilter(minEntropy));
        }
    }


    // Stolen from htsjdk.samtools.filter.OverclippedReadFilter so that I can use GATKRead
    public boolean filterOut(final GATKRead record, final int unclippedBasesThreshold, final boolean filterSingleEndClips) {
        int alignedLength = 0;
        int softClipBlocks = 0;
        int minSoftClipBlocks = filterSingleEndClips ? 1 : 2;
        CigarOperator lastOperator = null;

        for ( final CigarElement element : record.getCigar().getCigarElements() ) {
            if ( element.getOperator() == CigarOperator.S ) {
                //Treat consecutive S blocks as a single one
                if(lastOperator != CigarOperator.S){
                    softClipBlocks += 1;
                }

            } else if ( element.getOperator().consumesReadBases() ) {   // M, I, X, and EQ (S was already accounted for above)
                alignedLength += element.getLength();
            }
            lastOperator = element.getOperator();
        }

        return(alignedLength < unclippedBasesThreshold && softClipBlocks >= minSoftClipBlocks);
    }


    @Override
    public boolean test(final GATKRead read) {
        return filter.test(read);
    }
}

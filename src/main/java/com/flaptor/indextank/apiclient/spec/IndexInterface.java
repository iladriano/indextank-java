package com.flaptor.indextank.apiclient.spec;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import com.flaptor.indextank.apiclient.IndexAlreadyExistsException;
import com.flaptor.indextank.apiclient.IndexDoesNotExistException;
import com.flaptor.indextank.apiclient.InvalidSyntaxException;
import com.flaptor.indextank.apiclient.MaximumIndexesExceededException;
import com.flaptor.indextank.apiclient.UnexpectedCodeException;
import com.flaptor.indextank.apiclient.spec.ClientInterface.Query;

public interface IndexInterface {

    ClientInterface.SearchResults search(String query) throws IOException,
            InvalidSyntaxException;

    ClientInterface.SearchResults search(ClientInterface.Query query) throws IOException,
            InvalidSyntaxException;

    /**
     * Creates this index.
     * 
     * @throws IndexAlreadyExistsException
     *             If it already existed
     * @throws MaximumIndexesExceededException
     *             If the account has reached the limit
     */
    void create() throws IOException, IndexAlreadyExistsException,
            MaximumIndexesExceededException;

    void delete() throws IOException, IndexDoesNotExistException;

    /**
     * Indexes a batch of documents
     * 
     * @param documents
     *            an iterable of {@link ClientInterface.Document}s
     * @return a {@link ClientInterface.BatchResults} with the results
     *         information
     * @throws IOException
     * @throws IndexDoesNotExistException
     *             if the index name used to build the Index object does not
     *             match any index in the account
     * @throws UnexpectedCodeException
     *             if an error occurs serverside. This represents a temporary
     *             error and it SHOULD BE HANDLED if a retry policy is
     *             implemented.
     */
    ClientInterface.BatchResults addDocuments(
            Iterable<ClientInterface.Document> documents) throws IOException,
            IndexDoesNotExistException;

    void addDocument(String documentId, Map<String, String> fields)
            throws IOException, IndexDoesNotExistException;

    /**
     * Indexes a document for the given docid and fields.
     * 
     * @param documentId
     *            unique document identifier. Can't be longer than 1024 bytes
     *            when UTF-8 encoded. Never {@code null}.
     * @param fields
     *            map with the document fields
     * @param variables
     *            map integer -&gt; float with values for variables that can
     *            later be used in scoring functions during searches.
     * @throws IOException
     * @throws IndexDoesNotExistException
     *             if the index name used to build the Index object does not
     *             match any index in the account
     * @throws UnexpectedCodeException
     *             if an error occurs serverside. This represents a temporary
     *             error and it SHOULD BE HANDLED if a retry policy is
     *             implemented.
     */
    void addDocument(String documentId, Map<String, String> fields,
            Map<Integer, Float> variables) throws IOException,
            IndexDoesNotExistException;

    /**
     * Indexes a document for the given docid and fields.
     * 
     * @param documentId
     *            unique document identifier. Can't be longer than 1024 bytes
     *            when UTF-8 encoded. Never {@code null}.
     * @param fields
     *            map with the document fields
     * @param variables
     *            map integer -&gt; float with values for variables that can
     *            later be used in scoring functions during searches.
     * @param categories
     *            map string -&gt; string with values for the faceting
     *            categories for this document.
     * @throws IOException
     * @throws IndexDoesNotExistException
     *             if the index name used to build the Index object does not
     *             match any index in the account
     * @throws UnexpectedCodeException
     *             if an error occurs serverside. This represents a temporary
     *             error and it SHOULD BE HANDLED if a retry policy is
     *             implemented.
     */
    void addDocument(String documentId, Map<String, String> fields,
            Map<Integer, Float> variables, Map<String, String> categories)
            throws IOException, IndexDoesNotExistException;

    /**
     * Deletes the given docid from the index if it existed. Otherwise, does
     * nothing.
     * 
     * @param documentId
     *            unique document identifier. Never {@code null}.
     * @throws IOException
     * @throws IndexDoesNotExistException
     * @throws UnexpectedCodeException
     */
    void deleteDocument(String documentId) throws IOException,
            IndexDoesNotExistException;

    /**
     * Updates the variables of the document for the given docid.
     * 
     * @param documentId
     *            unique document identifier. Never {@code null}.
     * @param variables
     *            map integer -&gt; float with values for variables that can
     *            later be used in scoring functions during searches.
     * @throws IOException
     * @throws IndexDoesNotExistException
     * @throws UnexpectedCodeException
     */
    void updateVariables(String documentId, Map<Integer, Float> variables)
            throws IOException, IndexDoesNotExistException;

    /**
     * Updates the categories (for faceting purposes) of the document for the
     * given docid.
     * 
     * @param documentId
     *            unique document identifier. Never {@code null}.
     * @param categories
     *            map string -&gt; string with the values of this document for
     *            each category. A blank value equals to removing the category
     *            for the document.
     * @throws IOException
     * @throws IndexDoesNotExistException
     * @throws UnexpectedCodeException
     */
    void updateCategories(String documentId, Map<String, String> variables)
            throws IOException, IndexDoesNotExistException;

    void promote(String documentId, String query) throws IOException,
            IndexDoesNotExistException;

    void addFunction(Integer functionIndex, String definition)
            throws IOException, IndexDoesNotExistException,
            InvalidSyntaxException;

    void deleteFunction(Integer functionIndex) throws IOException,
            IndexDoesNotExistException;

    Map<String, String> listFunctions() throws IndexDoesNotExistException,
            IOException;

    /**
     * Returns whether an index for the name of this instance exists. If it
     * doesn't, it can be created byAlready calling {@link #create()}.
     * 
     * @return true if the index exists
     */
    boolean exists() throws IOException;

    /**
     * Returns whether this index is responsive. Newly created indexes can take
     * a little while to get started.
     * 
     * If this method returns False most methods in this class will raise an
     * HttpException with a status of 503.
     * 
     * @return true if the Index has already started
     * @throws IndexDoesNotExistException
     * @throws IOException
     */
    boolean hasStarted() throws IOException, IndexDoesNotExistException;

    String getCode() throws IOException, IndexDoesNotExistException;

    Date getCreationTime() throws IOException, IndexDoesNotExistException;

    void refreshMetadata() throws IOException, IndexDoesNotExistException;

    Map<String, Object> getMetadata() throws IOException,
            IndexDoesNotExistException;

}
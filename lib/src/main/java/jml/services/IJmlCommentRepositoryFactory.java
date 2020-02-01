package jml.services;

import jml.Lookup;

/**
 * @author Alexander Weigl
 * @version 1 (1/31/20)
 */
public interface IJmlCommentRepositoryFactory {
    IJmlCommentRepository create(Lookup lookup);
}

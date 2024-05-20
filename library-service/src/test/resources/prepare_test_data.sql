INSERT INTO library_service.libraries (group_id, artifact_id, name, description) VALUES ('com.infobip', 'libraryA', 'Library A', 'Description for Library A');
INSERT INTO library_service.libraries (group_id, artifact_id, name, description) VALUES ('com.infobip', 'libraryB', 'Library B', 'Description for Library B');
INSERT INTO library_service.libraries (group_id, artifact_id, name, description) VALUES ('com.infobip', 'libraryC', 'Library C', 'Description for Library C');

INSERT INTO library_service.versions (semantic_version, description, deprecated, release_date, library_id) VALUES ('1.0.0', 'Initial release', FALSE, '2023-01-01 00:00:00', 1);
INSERT INTO library_service.versions (semantic_version, description, deprecated, release_date, library_id) VALUES ('1.1.0', 'Minor update', FALSE, '2023-02-01 00:00:00', 1);
INSERT INTO library_service.versions (semantic_version, description, deprecated, release_date, library_id) VALUES ('2.0.0', 'Major update', TRUE, '2023-03-01 00:00:00', 1);

INSERT INTO library_service.versions (semantic_version, description, deprecated, release_date, library_id) VALUES ('1.0.0', 'Initial release', FALSE, '2023-01-01 00:00:00', 2);
INSERT INTO library_service.versions (semantic_version, description, deprecated, release_date, library_id) VALUES ('1.1.0', 'Minor update', FALSE, '2023-02-01 00:00:00', 2);

INSERT INTO library_service.versions (semantic_version, description, deprecated, release_date, library_id) VALUES ('1.0.0', 'Initial release', FALSE, '2023-01-01 00:00:00', 3);


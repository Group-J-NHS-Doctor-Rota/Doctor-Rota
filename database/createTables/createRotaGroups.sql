/*Rota groups are the 3 month long periods for which rotas are built*/

CREATE TABLE rotaGroups (
    id SERIAL NOT NULL,
    startDate date NOT NULL,
    endDate date NOT NULL,
    status bool NOT NULL,
    timestamp timestamp DEFAULT now(),
    PRIMARY KEY (id)
);
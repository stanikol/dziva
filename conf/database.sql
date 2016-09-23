--
-- sudo -u postgres psql -p 55555 -d templatesite_db -f conf/database.sql
--
DROP SCHEMA public CASCADE;
CREATE SCHEMA public;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO public;

COMMENT ON SCHEMA public IS 'standard public schema';

--- GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO dziva;
--- GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO dziva;

---DROP TABLE IF EXISTS message;
---DROP TABLE IF EXISTS account;
---DROP TYPE IF EXISTS account_role;

CREATE TYPE account_role AS ENUM (
    'normal',
    'admin'
);

CREATE TABLE account (
    id serial PRIMARY KEY,
    name text NOT NULL,
    email text UNIQUE NOT NULL,
    password text NOT NULL,
    role account_role NOT NULL,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);

CREATE TABLE message (
    id serial PRIMARY KEY,
    content text NOT NULL,
    tag_list text[] NOT NULL,
    created_at timestamp with time zone not null default now(),
    updated_at timestamp with time zone not null default now()
);


-- bcrypted password values are password in both users
INSERT INTO account (name, email, role, password) values ('Admin User', 'admin@tetrao.eu', 'admin', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQbLgtnOoKsWc.6U6H0llP3puzeeEu');
INSERT INTO account (name, email, role, password) values ('Bob Minion', 'bob@tetrao.eu', 'normal', '$2a$10$8K1p/a0dL1LXMIgoEDFrwOfMQbLgtnOoKsWc.6U6H0llP3puzeeEu');
INSERT INTO message (content, tag_list) values ('Welcome to the templatesite!', '{"welcome", "first message", "english"}');

-- SNC
create table small_pics (
    id              serial primary key,
    name            varchar(40) not null,
    base64          varchar not null
    )
;

insert into small_pics values (
1,
'pic-1',
' data:image/jpeg;charset=utf-8;base64,/9j/4AAQSkZJRgABAQEAYABgAAD/2wBDABQODxIPDRQSERIXFhQYHzMhHxwcHz8tLyUzSkFOTUlB
                                    SEZSXHZkUldvWEZIZoxob3p9hIWET2ORm4+AmnaBhH//2wBDARYXFx8bHzwhITx/VEhUf39/f39/
                                    f39/f39/f39/f39/f39/f39/f39/f39/f39/f39/f39/f39/f39/f39/f3//wAARCAJhAyADASIA
                                    AhEBAxEB/8QAHwAAAQUBAQEBAQEAAAAAAAAAAAECAwQFBgcICQoL/8QAtRAAAgEDAwIEAwUFBAQA
                                    AAF9AQIDAAQRBRIhMUEGE1FhByJxFDKBkaEII0KxwRVS0fAkM2JyggkKFhcYGRolJicoKSo0NTY3
                                    ODk6Q0RFRkdISUpTVFVWV1hZWmNkZWZnaGlqc3R1dnd4eXqDhIWGh4iJipKTlJWWl5iZmqKjpKWm
                                    p6ipqrKztLW2t7i5usLDxMXGx8jJytLT1NXW19jZ2uHi4+Tl5ufo6erx8vP09fb3+Pn6/8QAHwEA
                                    AwEBAQEBAQEBAQAAAAAAAAECAwQFBgcICQoL/8QAtREAAgECBAQDBAcFBAQAAQJ3AAECAxEEBSEx
                                    BhJBUQdhcRMiMoEIFEKRobHBCSMzUvAVYnLRChYkNOEl8RcYGRomJygpKjU2Nzg5OkNERUZHSElK
                                    U1RVVldYWVpjZGVmZ2hpanN0dXZ3eHl6goOEhYaHiImKkpOUlZaXmJmaoqOkpaanqKmqsrO0tba3
                                    uLm6wsPExcbHyMnK0tPU1dbX2Nna4uPk5ebn6Onq8vP09fb3+Pn6/9oADAMBAAIRAxEAPwDs6KKK
                                    ACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooA
                                    KKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAo
                                    oooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACii
                                    igAooooAKKKKACiiigAooooAKKKKACkpaKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooo
                                    oAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiig
                                    AooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKAC
                                    iiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKK
                                    KKACiiigAooooAKKKKACkJxS0hoAAc0E4pBxS9aAAGjIHeq9zcrCp5rFuNWwxANIDogwPejNc1Bq
                                    hLDJrbtbtZUHNMC0TRQCDRQAUZoxRQAUZpGYIMms261JYzjNAGnkUVhpqwLda0ra9SYDBpAWiaAa
                                    MZopgGaKKKAFooooAKKKKACiikNABmlpo4oPNAC5ozSCjFAC5ooooAKM0Yo4oAKM0UhoAXNFApGo
                                    AdSZpBS0AGaM0UUAFGaM0UAIzBRzSLIp71Uv5TGlZsV4xOM0AbhlApDMAKyjcn1qOW7OKTYGo14o
                                    PJqWO4SToRXK3V0204NJpt1K0oBJqUx2OvzRmo4CTGCakqhBmikHFLTAWikzRmgBaKSloAKKSigB
                                    aKKKAEozRRQAZoyKimk2CqYu/m60xGjmjNZ5u8Ux78AdaljRp5HrRkVhS6ntPWprLURM+3NFxmuD
                                    S0g6UgpiFzS0lFAC0UlFAC0UmaKAFopKKAFopKKAFooooAKKKKACiiigAooooAKKKKACiiigAooo
                                    oAKKKKACiiigApDS000AHU1HcSiKMk1IOBmsHXL/AGIVBoAzdT1Eu5UGs4bnOSarbzLKSaleYIuB
                                    QMtLgdDVi3v2iYDNZKTnOTTy+TmlYZ1MerhU5NNGsgt1rlHmbOAaUOwGc0WEdb/bS5xmkbW1B61y
                                    W585zTGZ3bg00B1c+sB0ODWLPcNLJ1qopZV5NSQfM9Ay3HCzDNX7GVoHGTRbqAgzUVw+1+KQM6q3
                                    lEiA1IetZGk3G4AE1sEZpkgOlFFIaAHUUgpaACiiigAppNOpOtAAOaTpSgYpGYKMk0AL25qF7qND
                                    gmqd9qSRKQDXK3eqP5pIbigDr5rsAfKapSamUPWucTVyVwWqCa+aQ8VJreJ0jazjvUY1o561z8KS
                                    ynnNaEVjuHzGgd4mxHrS/wARqzHqkTfxVif2ch/jp66cQPlbNAaG+l7Gx4NTiZcZzXNeXLAe9Nn1
                                    CRExzTIaR05uoh1am/a4v7wrhZtUnJ4Jqu2q3A9aZNj0T7VF/eFH2mL+8K88XV7j3qxb6nOzjJNA
                                    WO+V1bpTqwrG9O0bjV9dQj6bhQOwaoB5NYMRAY1s3syzR8GspYRknNIQrPUbHIqUxj1pjqMdaTQy
                                    rPtCHNLprDzhio7hMrgGl0yPZMCaSQM7GD/VCpBVeCZBEOak+0R/3hVkkhoqPz4/7wpfOj/vCgB+
                                    KMUzzo/7wpfNT+8KAHUUnmJ/eFJ5if3hQA6im+Yn94UvmL/eFAC0Um9f7wo3r6igBaKTevqKNy+t
                                    AFS9+6ay1OXrTvWBU4rLUfPTAkkHFU5OvWrErcVTdjmpY0QzplTzUmjqRcj61HKxCmnaVLi5H1pA
                                    div3RRTY23IDTulUIKWkFLQAlFLRQAlFLRQAlFFFABRRRQAtFFFABRRRQAUUUUAFFFFABRRRQAUU
                                    UUAFFFFABRRRQAUUUUAFJS0lAEVxII4mNcLrN15kxGa6nWp/KhIBrhZnMs5J9aEgHxsFXNRFjI1O
                                    fpiiNdqk0wFJAGKUHC1Gg3PTpDjgUwDOTSs2eKaOFoiG480AOZ8JinWqknJqGUfPgVdto8JmgENn
                                    OOBVnT4ixBqnJlpcVtafGBHmkMsk7ExVKXk5q3LVWXpSGWNLn2zAZrqo2ygNcTaNsmB9666wk8yI
                                    UEloGg0YooABS0UmaAFoopKAFpMUEgDJrM1DVY4EIVuaALlzdJAuSRXPahrechWrHv8AV3mY8nFZ
                                    TSNK2ATk0WAt3N/JI3UmoVVp+ADVvT9JmnYFgcV1djocUSAsOaAOYtNClk5wa1bfw8VILCupihSJ
                                    cKBT6BWMeLSVRelRT6fL/BW9RQM4u9tbyHkZp2n3EytiQmuulhWVcMBXOX0KxSnbSAsvNGy89apS
                                    xpIelMAJNSgBRk0XGQf2fGeSKQ6ZCR0FPe7VOM1F9vGetO4ANNhB6U9LKJDwKmhuEcdaRid3HSi5
                                    SI5iY0wtVI/Pkk4Jq5NjbzTrK4iR/mxQO5LBDMVwc1KbeRRVxL63UdRTvt1u3cUzO5meTIT3pBaS
                                    se9av2q2A6imNqNup6ii4zP/ALNc9aeunMnSrZ1eADqKgfWIh3FKwAYJgMAmmmCcDqajOtRg9RTh
                                    rUR6kUAN2XGeppzR3AHU09dUg65FTDVYD1IouIqBbj1NPC3Hqasf2jbjuKUalb+ooAgC3HqaNtx6
                                    mrB1O39RTf7Ug9RQBATOOpNKHm9TUpv4G7igXkHqKAIjNMPWmG5lHrU5uoD3FKJbY9xQBALuX3p/
                                    2xwO9PMlv6imtLbY6igCvJeM3WmJP7VMTbk9RTgIOxFMCu0uaYRnmrWyI9xQY0xSaGmUGTeKijHk
                                    SZq+yBelVrhAy1IG5p98rIATV77Qp71xSXDwvgGtW3u2dRk00I6Hzl9aPNHrWMbkjvSrck96Y7Gz
                                    5o9aXzF9axmuWHemi6b1oCxt+YvrTgwPQ1ifaWxTFv3RwCaVwsb1FRW0wljBqWmIKKKKAFooooAK
                                    KKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigApCcDNLUNy+yFjQBzXiK66rmuYXk7qv63OZ
                                    JiM1QXhKpAAO56dL8oxSRD5s0s3zMKGAkQxzSH5pKkb5EpsYz81IBJOOKVRtXNNkO56kk4QUARoN
                                    0laK4SKqVuuTmrEzYjxTAZF881blsNqYrF05d0tbijAqRoSQ1VlNW2GRVWUYpDKofY4rqtGl3xCu
                                    Rn4NdFoEnygUyToaSlptAC9qTFL0pM5oAM0O4RcscU2SRYl3Ma5vWNYwCqNQBY1XWFiBVWrkrq7e
                                    dyS3FRTzPM5LGmRxtKwVRTQCIrSttAzXQ6RoRkIdxVjRdFzh3FdTFEsKBVFAEdtax26BQoqf6Uh5
                                    pRSAM0tJigUAFFBooAD0Nc9qQ/emuhPQ1z+oHMpFAyoOBmqV5e+XkZq7KwSImsCdHuZ9q+tFgGPO
                                    0hyDSBm9a27LQXeLJFPm0JkGcUmgMeCdkYZNbcEgkj96xLq1aF+lXdNZjwaEMs3eViJrCWZzNgGt
                                    +/H7g1z0RCz8+tMTNWKGR1zuNTJbyf3jRby/KKtoSaGIg+zyEfeNRtZyN/Eav4NIcilcaM7+z3/v
                                    Gg6e571fMmKb51O4zNfTX9aj+wOO5rUaXNRGSkIpCycfxGj7LIP4jVzzKUNmgCj9ml/vGgW0v941
                                    oAUuKB2M428v940n2eT+8a0CKTFAWKIikXuaQrL6mr+BSFBRcLGefN9TQGmHc1oeUtIY1ouFikGm
                                    Pc0u2U/xGr6IlBUZouFigBKO5qRXlXuauBVpQqUXCxVFzKPWpUu5M1IVT0p6rHjpRcLEb3Tbarm6
                                    LHFWJFUiqzQdSKAZBJIDJWnZcqKwpsibFbmnA7BQJFzbk0uQvWiVxGuTWRd6hgkA0IbNYyKe9Ku0
                                    1zYv33davWt8WIyaBGx0qtNzIKnicOtRyr81IZraafkArRrM03oK0zTRLCigUUwFooooAKKKKACi
                                    iigAooooAKKKKACiiigAooooAKKKKACiiigArM1i4EduRmtOuX8Rz7QVzQBy9w/mTsfemN0xUaHM
                                    hNSdTVICVOEpi/M1DHC4pIumaGA64b5cUqcRVCx3NUpOExSAjTls0+Rs8UkYwDTc5egC1bjC0yd8
                                    8U9OFqu/L4oA0dKX5s1rseaz9NTCg1oMOaTGgLcVWlqzioJlwKQzOuK1dDl2sBWTcdas6TJicD3p
                                    kncKcoKXFNh5iX6U6gA61HLIsKFmNOkkWJCzGuV1vV92URqAE1nWd2UQ1zLzNI5Lk0FmZ9zmkKmV
                                    tqUAKqmVwqV1OiaNwHcVHoOik4eRa6uNUiUKuBigAjRYkwo4prToOtPdl29azLgEtwaALv2pQaVb
                                    yInGeazGB2HmsiSeSO44JxmkB2CsGGRS1Q06cyRjNaFMApBRQKAEb7prmL6T/Sjn1rqG+6a5TWFK
                                    zEigY24w0XFM0i0VrjLDvTbZxIuGNWIX8iTK0XA6iNAiAAUrqHGCKpWl4HUbjVlp1AzmlcRg63Yj
                                    OQKoWcGytrUJxIMVlGVY6BiX7AQHNc7DiS4/Gr2pXe9SoNULBT52aoTOhtbcbRV+OIAVWtT8oq0D
                                    xSYCMAKik6U5s1FIcCoKK5JLYqXyMjNV9+JKvJICopoCHyKaberW6jNMCoLbmnGAgVbyKMg0gKgi
                                    NDIRV0AUxwKAuUGBphDVdKAmgRCgLlMA0uDVlowKaRQFyDmkINSGnAZp2C5ANwo5qcqKTbRYLkBJ
                                    p8YJp5UVYgQUWC5AYzT0i4qy6ADimDiiwXKkw2moHkwpqxcAmqrr8pzTQMypWLXH410WnD92K5u4
                                    O2bNb2kzqUAJpSEiXVZCsZxXNEl3Oa6fU4i8ZK1zjxmNjkUR2BiiMBc0QttkGKTzARgGpbaBncEC
                                    mI3rBiVFSzNhxSWcfloM02b55Rikxo2dOxtBrRqlp8e2IVdoQMKKKKYhaKSloAKKKKACiiigAooo
                                    oAKKKKACiiigAooooAKQ0GigAFBoooACcKTXDeJJSZyM967O6fZET7VwGty77g/WhAZ6jilT71C9
                                    Kcg5pgJJ1p6cLTG5NOzgUAMH36eTTFHNKetADx92mRjL0pOFoh+9QBZ6Cqx5lqxIcLUMI3SUAjbs
                                    DiMVbY1WtSFQVM8gAqGWhwaklGVqJJlL1Zd08uhCZj3HWl05sXI+tLdjJJFV7Jyt0PrVXEeg27Zh
                                    X6U93EaljUNj81sprJ13UxChRTzQIra1rAwY0NcwQ7uXekZ2nmLseKdNKNm0daBkEh8xtq1u6FpJ
                                    Lh3HFUNJs/MmDMK7JJYrW3wMZxQImkuY7SLauM4qh9vZmzmqskhuZDg05YdtKwmWHvGPekExbrUO
                                    wZqVUwKpIaCWXC1mHEk341au22qazbeTNx+NDQzq9Mj2oKvk4qpYMDEKtkZpCAUUgGBSigAPQ1z2
                                    qRh3NdBIcKawL5syGmkBhyK0TcVLHcgD5utLdGs12OaTQGxHeYPBqZtRIHJrAErLTWnZh1pWGbMl
                                    +Dnmsq7uix+U1VaU+tR7tx5pgDMW5NT2J+cVC+AtS2PMlUI6W15UVaA4qnanCirq8ikxIbioJulT
                                    tVaU1mWU9vz1bjHAqsD89W0PFMCVelBIpM00mgqw4GnCod2KcHpCaJu1NNN38U0vTEPA5pSKiVua
                                    kzQAhGaicVNTHpgVyKVTSsKQCgBSaTNLikA5oACMmp4gahJwamiegCTB71G3FSFqjbmgCKQjFUp+
                                    hq5IKqXAwKAMmcZanW1w0TjBps3LUwoaAOltbtJ49rHmoLzThKPkrFhneE8GtS21I8bqBFePRnD5
                                    PSta1tUgX5qb/aKkdqgmvc9KAL8sgxhaksrZpHyRVfTV89hmukggWJRgUALDH5agVJmjNJimIWii
                                    igBaKKKACiiigAooooAKKKKACiiigAooooAKKKKACkNLSGgAoFAoFAFLVG225+lee6g2+c/Wu711
                                    9tsfpXATndKfrQgAdKUU09Kd2pgIOWpXpF60SUAKnSkP3qB0pByaAB+lPgHNMep4BxQAsvSo0Ow5
                                    qWXpVcnNMRbW8I4zU/2gsnWsvHNTCTC1JVyU3BV+tSG9YjGapE5NKBmnYDTgYSjmmeTsuQR61Xjm
                                    8oVYW4DDcTzSsB0qamtvZ4zziuR1C6a6uTzxmi4u2f5QeKgRcfMaLCJchEx3p1paGeTNQAGSQCuh
                                    02AIgJFOwE9rbi3TpVe6nZ32g1flOAaymObj8aYGjYptXJqy/XNNgTKCpHXAoERLyanA+WoUHNTH
                                    haY0Z98ODVCGLD7quXrc0lrHuFJsZqWNzsUAmtJbpT3rFWMr0pwLilYRt+euOtMNyoPWsoM/rTX3
                                    EdaLAaE92pXANZ0g8w5qEh8808MVFMRRu4eKypUINbk3z1Tlt89qYzKIzUbKe1XntyO1SQ2m/qKQ
                                    GU0ZNCQnPStprH2pVswO1IDFlhbbUunxkPzWlNbDb0qvCvlyUDNiBcKKsq2BVeA5SnEmmxIkZxUM
                                    nIpjMQaTdkVmUVyMPVhTxURHzVKBxQCJA1BNR0ooLBjSL1pcUdKRLHnpTKM8UmaYD0HNTAVCh5qY
                                    GgAIphqTrSbaYDNmajZcVYpjrmgCEUoxQRimDOaBBJ1qWFajINSxnbQBKVpjUpkqNmzQBFIap3Lf
                                    LVx+ap3C5FAzKkPz1In3akMGTR5RApiISoJo246U9kINAB70CFiDE9alfjFMRgtKxLkYoA6LQxwK
                                    6Relc5oZCgAmujHTigAxR0oooEJmlNAFBoABS0lLQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAlF
                                    BoNAAKM4oBzQRxQBh+I5P3BHtXCk5c113iOT5CK5BeSaYD+tOPApq9adJ0oAFHemvyaen3aaPvUA
                                    OxhaRRzmlk6Uq/doAjkPzVZhHy1Ub79Xbf7tAEcxxVfvU9x1qEUCAU/GRTRT+1AxoAFOApvenjha
                                    YDT8xxTXV0HGcUI37ytdYo3t8nGcUDMaNSxpZGK8VYQKJSKbcR5OaAsTabF5jg108EQRBWHpIAIr
                                    oR90UxFe7+VM1lQjfcfjWpfn91WbZjM340CN2IbYxSStxUiL+7FRzLxSAjjPNTt9yoIl5qZ/uUDM
                                    q85arFmAEqpccyVatfu1Iy2DmnYFNUU4iqENPtRzS0uaAI9vrSMmaeTmk7UhEPlimNHU560Y4oGV
                                    TAD2p8cQWpsc07FO4EZUUnlipCtA4oAglhBWs+SECSthhkVVkhy1Sx3Ftx8lTbBimRxlRUo6VRKK
                                    7xZNNEVWKYW5qbFlZ12mlFLJy1AHFSwACl4oxSEUh3Amm9aXFB4oAWkptOFAD0HNTAVGtSCmMDRm
                                    g02gQueaUc02gGmIR0zTFTB5qYGkYUANCg0jLgU9RTiKAKpU5oINWNoprrxQBVbiqsrbuBVicnpR
                                    b2xc5NMCmvy9amSMPVyWyHamxxeXTArPbADpVKVNpxW0y5FZ08fz0mxFAxsTxVm3iPerMUOR0qVI
                                    cHpTGEUrwMCOldBpt+JVAY1jGLctRRSNbzDmkxHY5yMigVVsZxNCKs9KQgzS0EUUwAUtFFABRRRQ
                                    AUUUUAFFFFABRRRQAUUUUAFFFFACGgUtNZgikmkAkjiNSSaz5NUQZGRms3WNU2ZVTXO/aneTOTQB
                                    e1q583NYI71oXOWj5qhjApoBVoahKG60wHjhKYv3qeeEpkfWgB79KVfu016cPu0ARH79XYPuVSH3
                                    quxcJQBDOfmqGpJz81QmgB605jxTFNDGgBV61Kfu1CnWnuTigCPo2al+1SBdq1EBk1tadpyypkii
                                    4zKhR2kBI61o3FsfIBrTXTlVuBS3sWyHFFxlDSEIbmugUfKKx9NXnitlfu0yShqJwlUrDmWrOpt8
                                    tQaYuXoEbyH5BTJvu08LhRUM5+WkARDNOm4Q0W/IouOENAzGmOZau233aoSHM1aFt0pAWRTqZS1Q
                                    ATTTSmjFADad2pCKO1IBO9L2pKd2oAbjmnUlBoAdTSKUUoFMBuKjJG8CrBXiqkoIekwNFYVMOaqS
                                    KVzViCb93imthqASKgPPNBxT5Yj1FREEUmURsOaXHFAPzc1PgEVIIrHNJzU5UUbBQMrnNJVnyxTT
                                    GKBkIFPUU8JTgtAhFp2aMUUBcXrSYozijNAhMUYpc0UwDFIadmkNACA0pNIKUigBAaRzxS1HIeKA
                                    IWUO4rUtrYLGDWbGDvzWgLjbHikBKYwarzQgUz7SRTWuC3FMQpUbaz7hcNWggLVVu0xTsA22ANWS
                                    mKp2rfNWjjK0XAagFV7qHJ3CrK8UkvKGgCXSZ8MFJre6iuTtmMU2a6C2ug4AJosItE4NOHSk4bml
                                    oAWiiigAooooAKKKKACiiigAooooAKKKKACiiigBKpapN5UBq6eBmue1y7+UpmgDmb13nmPPeiKL
                                    aBmpI9pJJqYAEcU7AVLtsJVEHINXbs8YqmOlMB8Q4prfep0XApudz0gHN92iEUSDC0Q8A0ANf71P
                                    H3aY3LU88LTAjX79XY/uVSQZar0Y+SgCpMfnpmKdNw9NzQAUnU0vUUBaQD1FI5Panr0pDwaAGwj5
                                    gTXUaU42CuZJyOK09PuDGuKQI6ldp5rN1eRTHhari9akEb3LZNK5Q7SYyRk1r7cCobaEQr0qweVN
                                    UiTF1Q07SFyah1RvmxVrRloA2WX5RVS5HFX2X5apXVAC2o+Wm3R+Q1JaD5ajvRtQ0gMMnM1aduPl
                                    FZecTVpQSjbSGWgKeq1CJhUgmAFO4h5SmY5oNwDTTIKAFYUAcUm8GgOKADFLjik3CjeKBiUHrSjm
                                    mt1poTJFGRS4xSx9KcRzVCAdKryrlqtbeKgkHzVLAciYSoDJiTFW1H7uqLj99QUi7gFMmq8m3tUr
                                    kiKsx5iJMVIx7qd3FSANinRYbmrClaQIrbWow1WsrUbSKKYyLBowalV0NPwpoGVwDS81PsWl2CgR
                                    VOaOas+WKURrQSVcGjFWvLBo8kUDKuKKteSKaYhQBXprNVoQA0htQaAKynNSY4qYW4WmPhaAImFV
                                    5CRVo4IqrNQAsB3GrYiJqvaLg5NXlcZxTsIja2GKrGEq1aQGahmXBp2ENiXC1XvEypq5GuVqC6X5
                                    SKYGXBxJWqnKVlL8slakPKVAxQKY4qYCmuuRTQiqVGeKdHK0RzTG+VqdIQ0dNjNqwuPNXFXqxdHy
                                    DWzSELRRRQAUUUUAFFFFABRRRQAUUUUAFJS0lABRRS0AMkOI2PtXEa1PmcjNdjfSbIG+lee6jIXu
                                    z9aaAQSYFT28m4VVYfLU9mvBpgR3Z5qqOlT3TfPioBQBIg+WmIP3lSL92mJ9+kA6XpREPlpJTTo/
                                    u0AMx89OfpTR9+lc0wFhHNXUGFqrAOat9FoAz5/9ZTMVJN9+mUALjilo7UlAD1PFIeaO1IDzQhMA
                                    MVes13EVSzzWjYDLCiSHE2LezDgcVoxWyxjpTbMYUVbIzUWKYzbkUbMIakUU6QYjNMk5bVP9bWho
                                    q8Cs3UmzP+NbOhr8op3GazjCVnXVakg+Wsy5HNJAiSzHFM1AfIakteBUGoN8hoYHPyHEtW4icVTb
                                    mWtG3T5akaEAbNSANipAtSheKBFUK2afhsVNt5p2OKYysQ1J81WcCnbRTEVctSruqxtFGwUgGpml
                                    NOAxTGPNWIniHFPxzSQDIp5HNIB3aq0g+arXaq8g+agZMi/u6oScTVop/q6zZv8AXUCLL8w1iznE
                                    tbgXMP4VhXfEtIZoWg3rgVYMDDmodO+7mtAsSOlAMpmJjUT2sh6VoDinhx6UWC5krbyKe9S+XIBW
                                    gee1IMelFh3KKxye9SCN6ucAdKbu9qAKpjek2PVz8KaevSgCod601pGqSdwOtQ7g1IdgEr0GRqTv
                                    S54oCwCVhR9obNMJpMc0xEjTtULMzmlanRjNAhFBxTGj3GpXbbTA+aAJEAQVLB8z1WZqms2/eU0I
                                    01jwKgnSrn8IqvLTYEUPpUV4OKsRDmoroUIDDl4krQtGytU7pcNVmwOeKkovYpGHFSMKQjimiShO
                                    tVmkwMVenXrWY2TJj3pMaN3RxxWxWZpKYQVp0IGFFFFAgooooAKKKKACiiigAooooAKSlpKAClpK
                                    WgDK1qXZCR7Vws/zTk+9dh4kfalcj945oARulWLc7UNQ7c1InCmmBTnOZTSd6JB+8pM80wJAflps
                                    f3qCeKSI/NSAWU809Pu1FJ96ng/LQA1fvmhzzQnLUkn3qYE8FTu2FqvDUsp+WgCrIctTR1pW5NCj
                                    mgB56UgFOxxQKADHFNUZNPPSoweaSBju9amn/eFZQ5atjTk+YUNjidJaD5BVrpUNoMIKnxk1FymC
                                    8mic4iNOC4plz/qTQScjfnNz+NdFoa/uxXN3nNz+NdPog/dCqA1JPu1lz8vWpJ9w1jzN+9qRot26
                                    /JVLUPumtCD/AFdZuotwaYjD/wCWtaduflrN/wCWlaEHSmNFkU8U0U4VICd6eOlNqVBxTAiIyadt
                                    OKcRg07HFMRD3p/akxzUm3ikBFjmo34apc4NRSfeqhFu36VIetR233akPWgY4DioJfvVZA4qtN96
                                    gZMn3Kz5h++rQQ/u6z5f9dQIuAfuPwrAvf8AXVvk/ufwrCuxmWkBf00/LWi7BRWfYDC1PcPgUAxJ
                                    LjFJHPk1VJ3UgypouFjWVgVpu7mq8Lkip160XHYk7UwnBqQDioXHNAD/ADBihWBNRbTTolO6gCnq
                                    HB4qG2Bap9RHFV7I/OKQ7l3yOKQQVdA+SmgUBcpi3zQYCO1XR1pxAxTFcy3iPpRFGa0jGpFEcS5o
                                    EZksRJpUt29K0zEu6niNQKAMpocCorc7Za0rhQFNZ0Y/fVYjch+ZBUUy4zUkBwgpJPmBqWBWhPzY
                                    ouRRGv7yluRxSWwGLeDmn6d9+i66mksD+8pDNkjgUmOKkAyopuMVSEU7hetZhX97WzOvymsiY7Xp
                                    MaN/TPuCr/eszSG3JWn3oQMWiiigQUUUUAFFFFABRRRQAUUUUAFIaWkoAKO1BpGOFoA5jxLJniuc
                                    jHFbfiB8uaxovu0wHqKSQ7RThTJjxSGVHOWpoHNKetIOtUIV+lJHwaV+lNWkAr8mlB4pDR2oAEOG
                                    ofrSL1obrTAmiNOlbimRmlk6UARGnRjJpO1LH1oAkbgU0UrHikFIBT0pgHNPPSo80wY5eGrb0zki
                                    sNT81bmmdRUyKidNbfcFWRxVW3Pyip91QNjxyaivOITT1bmoL5v3RoQdDlLnm6/Guo0YYiFcrMc3
                                    P411OkH90Ksk0pD8hrHk5mrVlb5DWQ7fvqSGXojhcVm6j0NX0bis7UX4NAGTHzJWlCOKzI2/eVpQ
                                    nIpgWlxS8UxRTiKkTDPNSK1RAc1KtMEO60ZxQaaTQMctKTSKeKQmgQxutRnrUh61DIcNVCLtv0qR
                                    utQWx4qZutADw3FQTfeqUGoZTzSKRNH/AKus+biarsZ+WqU3+toAt9Yaw7skS1uA/uaw7w/vaQGh
                                    pp3AVPeoccVBpp4rSdA680gMZARSgMWrQa3GafHbDrTC4lvENmTSjh8VI3yLgVEDlqYXJ88UBQaQ
                                    dKM4FADJDg0+PpmoHOWqxGPloAz7/nNVrMYerV7xmq9o3z0gNYE7aaOtPUjbSd6YCgU7FJSigBQK
                                    OlBOKaWoEB607NN60g60ARXGSprOTiateZPkNZJ4mqkSbUAzHTX4BpbU/u6V+hqWMqRMfNqW4GRU
                                    ScS1NNytCGY90nWq9mcS1bu+hqnan99QB0UXKCmnrSwcxigjmhCIp/uVi3Q5zW3OPlrHuxTGjQ0R
                                    u1bdYGjNg1vjkVIMWiiimIKKKKACiiigAooooAKKKKACiikoADTJuIzUlRXBxC1AHGa4+ZTWbF92
                                    rWsPmY/WqcR+WmBNmoZTxTi1RSHikMgPWjvSd6O9UIVqRaGoFIANL2pvendqABetI3WgUHrQA9Kc
                                    5pq0MaAE7ULR2pBQBITSA03NAoAeTxUdPbpTKYCx/ere03tWFH96t3Tu1RIqJ0MBwtTFqrRN8tPL
                                    1BTJ1bmqt+37o1IrVVv2/dmhC6HOvzc/jXU6WcRCuUzm4/Guo05sRCtCTQkb5TWWcGar7N8hrMyf
                                    PqUMvD7tZuodDWiOlZ9+ODRcDKiH7ytSIYWsuL/WVpR5xQgJ1ank1GlSjpQDGU4HFIcUhamCHhs0
                                    49KiVsmpCeKBgGp1Q55p4bigTEY4NQuctTycmpEhyaokfAcCp85pgTaKM0APzUEn3qkLcVA7fNSK
                                    RZT7lUpf9bVxG/d1Rk/1tIZbA/dVjXiHzK2kP7us67jy2aAJdO4WtLOKx7Vyhq/9oBWglloc07OK
                                    pC5xTvtINAiw43CoQhBpouRmnmYYpgPFLgkVB54pwnFAwZTmpo+lQGYUqzgUARXq8GqVtxJVq6l3
                                    KcVSt3/eUDNlPu0opsTZWnZoAeDSg1ETTlagBXNRbuakY5qPHNAE6dKeAM1CrYpQ/NAEs3+rNY0n
                                    E1bLnMZrHm/1tVEk1LU/u6e54NRWh+SnOeDUvcZXVv3tWHOUqnn95Von5KBmbd9DVC3OJqvXfeqE
                                    P+tpAdFbnMYp561BbH92KkLc0CYTfdrHvBwa1pD8tZd30NFwQ/Smw9dGhyormdNOJBXSxfcFMGPo
                                    oooEFFFFABRRRQAUUUUAFFFFABSClpKACobw4t2qeq97/wAez/SgDgNUbNw31qvGflqTUv8Aj5b6
                                    1Ch+WnYBxPNNY8UUxqLDuRnrS0GgUxCGl7UjUCkADrSmk70poABQOtFKKAHUjUCg0AIOlFJSigAp
                                    R1pKUUADU2nmozQBJH96tuwPSsWHrWzZcYqZFxNuNvlp+6q8bcVIGrMpkqtzVa/b92alU81Xvz+7
                                    NNEswQf9I/GulsD+6Fcyn+v/ABrpLE/uxVkl7PyGqC/6+rLt8pqlGf3tIZoFsCs2+fg1fJ4rNvjw
                                    aVgKEJ/eVqJ92syEfPWjGPlp2AlQ1JUS1JmgGIaQrmgmlU0wQiqQakJ4pRimtQUNFMdsU/NQy0Es
                                    mg+ZqvABcVm25wauFjincRNIwxUIOaYWJpRRcViTHFQuvNPzUUjc0FIlQ/LioXA31Kv3KrMx30hl
                                    tPu0jwhxSIfloDHNADBajtTvsxqUNS+bQJkP2U0n2Y1Y82jzBTEVxbHNOMBxU/mCjzBRcCt9nNHk
                                    tVnzBRvFAFcQGkMBq0JBS+YKAKbwHYapxxFZa12YFapyYDcUDJ4+Fp26okbinA0rgPzS5pmaaTQB
                                    JupM01aVjTGODU5etVw3NSK1IVyyW+Q1kzn97WiT8hrOk5lq0SX7Vvkp7twahh4WnE8GkxplfP7y
                                    rmf3dUM/vKs7vlqRlO671Qj/ANbV+56Gs5P9bQBvWx/din55qC2PyVITSAWQ8Vn3XSrpPFUbnpQA
                                    af8A60V00X+rFc1Yf6wV0sP+rFNCY+iiimIKKKKACiiigAooooAKKKKACkpaSgBar3v/AB7P9KsV
                                    Xvf+PZ/pQB55qX/Hy31qup+Wp9T/AOPlvrVdT8tUgFzSNQKGoAYaQUNSDrSAU0opDThQA3vQaDSG
                                    gBaWkooAeKDSCigBDQKDSCgBaUU00ooAcaYetONNpgSRda2LM9Kxo+tatoelRIpGwjcU/dUEZ4qQ
                                    GoLJ0PNVr8/IamU1Vvj8hoRLMeP/AF/410VmcRiucj/1341v2p/dirJLbH5TVJD++qzn5TVZP9bS
                                    KLueKoXo4NXc8VQvTxTApxHD1oxtxWXGfnrQh6UAWBTqaDS5pCYGgUtFMSFBoJpCaSkWITUbmnNU
                                    TGmSyWE81azxVKI81aB4piHGnDpTM04HigBSaglPNSk1WmPNIZYQ/JVdj89SofkqE/foKLSH5aO9
                                    NQ8UE0ASZ4ptNBpc0ALS03NJmgQ/NGaZmjNAD80ZpmaM0ASZozUeaM0APLcVXc81LmopDTGPQ8VK
                                    pquhqZTxUgOLUmaaTzRmmBIGxQTmoS1OVqCRSOacppjOKRX5oEWc/KaoSH95VsN8tUnP7yrQmXYj
                                    8tOJ4NRxn5aCeDSYIrk/vKsA8VUJ/eVZH3akoguDwaoL/rKuzng1SX/WUAasDfJTy3NQwn5aeTSW
                                    4x5PFU7npVnNVbjkU2IdYf6wV0sP+rFc3YD94K6WL/VihCY+iiimIKKKKACiiigAooooAKKKKACk
                                    paSgBar3v/Hs/wBKsVXvf+PZ6APOtU/4+W+tV1+7VjVP+PlvrVZPu1SAUUMaKRqAGmminYpMUgFN
                                    KKQ0CgA70ho70GmAUUUUgFFOpop1ACGkFKaQUABpRSUCgBxptKaQUwHp1rStT0rMTrWhbHpUyGjV
                                    jbipQ1VozxUgNQWTq3NQXp+Q09etRXh+Q0ITMqM/vq3rU/uxXPx/678a6C0/1YqySYn5TVRH/e1a
                                    b7pqmn+tpMovqciqd6ODVtW4qpecigRnx/frSh6VmxKS9akQwvNAx1KDRkGlxSExRQTRSGmCAGlz
                                    TQM0uMUhgRUEnFWKrzCmIWHk1b6Cqlufmq21MQmaeDxUZ4pwPFACk1Xm61MTUEp5pDJU+5UZ+9T4
                                    /uUwkbqAuTA8UE00dKKAuOFOzTQaKAuOzSZptFAXHZozTaCaQXHZozTM0uaAuOzS03NGaYXBqryG
                                    rBxiq8vWgLixmpw3FV46lJ4pDuOByaQmmqeaVuaYBnNBOKQAigmgQxmNCk5oJApVwxwKBFhT8lVH
                                    P7yrYjYJVNv9ZVoRcjPy0jHg0kZ+WkY8GkwRWz+8qyG+WqZP7yrCn5akojlPWqo+/VmQ1WH36AL0
                                    TfLUmagjOBUgNJbgOJqvMc1KxqCSmwLNgPnFdFF9wVz+njLiugj+4KEJj6KQUtMQUUUUAFFFFABR
                                    RRQAUUUUAFJS0UAJUN4M27VNUdwMwtQB51q6YuG+tU0+7Wnri4mNZkf3aYC4oNLSNQAmeKb3pTSC
                                    mAGig0UAJ3oNHeg0AKKQ9aUUh60AOHSlFIOlKKQCNSClNGKYDaUdaSnKOaABhTakYcVFnBoAmUVc
                                    tzzVFW4q3a5zUlGpH0qQNUafdqRRUgSoar3n3TUw61DdfcNCGZUX+urobT/ViudT/XV0Fmf3YpiJ
                                    3+6aoof3tXW+6apKP31JAy6Kr3P3asA8VXuelMCnDw9Xy/y1np9+rIJNAyWMnNT9qjjWpgKBDBnN
                                    O7U4CgigBgOKUnNLt5pwWkBEahk6VacVA68UxEUH36uE9KpB9jVMJt1MZO3ShelMzkU4GgBGqCXr
                                    U5qGQUASxt8lRkfNTo+lITzSAlXpS4pqnikL4oAdSZqIy80vmikBJmjNReaKBKKoCXNI2aYJRSmQ
                                    GkAozTqZvFHmCgB+aAaj8wUCQUwJu1V5OtPaUBar79zUgJVpxNPjjytKYTQIiBoEmDUhiIFReSxP
                                    SgB7SjFRh808wNikWAimAxsmpLZT5gzS7MVIh280AaLhfJrJlXElWftJK4qq7ZeqETIflpG6GlXp
                                    SN0NSxlM/wCsqwOlVz/rKnXpSGNfpVdR89WH6VAn36ALA6U8GmilFAAwqOThamI4qGYfLTAt6Yfm
                                    roE+6K5/Sx81dAv3aBMWloooEFFFFABRRRQAUUUUAFFFFABSUdqKAFqOf/VGn02QZQ0AcF4gGJTW
                                    RH92t3xImHNYcY+WmA4UjUUjGgBKSlpKYAaKDRQAneg0d6DQAopD1pRSHrQA4dKUUg6UopAIaUUj
                                    Uq0wGmnJ1pDQnWgCR+lQ4yambpUJOKAHp1q9AyqKz1ViakJcCkM1luB61YjfcK58SuG5rRspixAq
                                    RmotRXX3DU6DIqG7HyGhAY6n99+Nb9n/AKsVzoP7/wDGuisf9UKYiwfumqa/62rh+6apr/raQ2Ww
                                    Kr3XCmrIqtdcrQBSiOXrQijzis+Ff3lacbYFAywqACnYFQ+ZR5hoETYFLgVBvNHmGgCbFKBUKyU7
                                    zOaQEhWmtGCKaZKUSUxFC4iOeKbCCDzV1sMagcBWpjJVHFPpqHilNACVFIeamxVeUc0ASJ0pD1pY
                                    /u0vekA4dKrSvzVk/dqlIfnoAcAWp3lmpYV+WpKEBX8o0eUasZozVAQeWaNhqfNGaTAg2mkKGrPF
                                    GBQBV2GlCGrBAowKYFZkYimxRHfVzihRg1LAswJham2Cqom2ij7QaBFloxUiRpjnFUvPJo88+tAF
                                    1o17VEYqg880v2g0wJDCDQ0A2VF9opPtGaAI/KwTUDLhqtlxiqzMC9AhwJApCeDTwMimkcGmMqD/
                                    AFlWR0qAf6yrAoGRydKhjHz1PIOKiiHz0gLAFOxS4paQDT0qKQZFStUUnC0wL2lj5q2+1YWmP81b
                                    q8rQIUUtIKWgQUUUUAFFFFABRRRQAUUUUAFFFFACGjrxRQOtAHIeKYtpJrmo/u12HihNyVx6jBxQ
                                    AuKaRUh4pKoCOkpT1pO9IANFKaQUwEIopTQKAAUh60opDQA4GlApop60ANalWhqFoAGpq8GnNTRQ
                                    A9m4qLPNPPSmY5pAXrNVc4q49qvpVOxOHrVOWxSAoSWY25AqC3by5tta0gxGayUH+kfjSGbsJyoq
                                    O65Q1JB90Uy5+4aBvYwiNs/410Fg2YxWDJ/rq3NP/wBWKYkXCPlNU1OJau/wmqWP3tSMtjkVBcdK
                                    nSoLnpTAqRn56uKapRcvV1RxQA8U7pTRTiKAAGlxmmgU4UAHSgdaKUUAFJnFLTTQIWoZfvVMKhl6
                                    0DJIjkVIeKji6U81SAdnioJOTUvaoW+9SYEqcLTT1py/dpp60gHH7tUn+/V0/dqk336YFuE4WnZz
                                    UafdpU5NADicU0NUjJkVEVwaLgSDmlIpq04UAAFFOpKQCYpMU6imAgFOzSUUAIaMUtFOwABQRS0V
                                    ICYoxS0UxCYpNtOooAaQcVWH+sq05wtVUOZKALS8CmvwKfUcpwKYEKrl81PtqGJvmqz2oGQyHApk
                                    I+almog60ATmlxQRThRYCNulQycrVhxUDjiiwibT22yCuiiOUFc1acSCukgOYhSYiSiiigAooooA
                                    KKKKACiiigAooooAKSlpKACiiloAxtei3wk+1cJJ8k5HvXpOoReZbt9K861GMx3Z+tADWpO1A5FF
                                    UBG3WkFOI5pO9IANIKU0goAU0gpTSCgAFIetKKD1pgAp600U5aAEahaGoWgAaminNSCkAp6Uzoak
                                    PSoz1oAntpNrVppdKAMmsZcg8U47z0oA2JrtShANZ8Z3TZHrVRmcdas2R+cZpDN+2+4KLn7hp1uc
                                    qKbc/cNIb2MKX/XfjW3p/wDqxWJN/rvxra08/uxTCJf/AITVI/62rg+6apMf3tIC0hqK5+7UidKh
                                    nOaAKsX36vjpVJRtarKNkUASjrThUQNSKaAFozQTTc0AOpc0i0tABmkooNAhRUMvWpN1QuctQMmi
                                    6U49abH0pe9UgHHpULfeqY9Kg/ipMCdfu009acv3ab3pAD/dqkT+8q7J9yqI5kpgW1+7Sx8GhR8t
                                    JjFDAs7hiomIJqPcaUZNAEi06mCnUAFFFFIBaKSlpgFJS0lMAooooAWiikqQFooopiCgdaaTQDzQ
                                    ATfdqpH/AKyrUhytQRr89AFoVFP0qUVFPTAiiHNWx0qvGKnHShgV5+tLAKbP96pIRhaAJjQDRQBQ
                                    AHkVBJwKnbgVVc5OKAJ7RcyCuhgGIxWPp8JLA4rbUYUUmIdRSCloAKKKKACiiigAooooAKKKKACi
                                    iigAooooAZKN0bD2rgdci23BOO9egEZGK5bxHZYUuBQByqVJt4qKPIJBqZenNUBC3Wm96dJ1plAC
                                    mgUDmikAGgUtIKAAUho70GmAop60wU5aAEekWnOKatACmmilamikA89KZ3p2aSgB8YBati0s1kUV
                                    iK21q1bK9KACgBb6xCdBVSCMh61ZJPPFRLBg5ApDLlqMIKLn7hp0AwKLgZQ0DZz83+u/GtnTz+7r
                                    HuBiatfTuUpgtDQH3TVJ/wDWVexhDVBjmWpYF1B+7qs/LVZU/JUYjy2aVwK8i4FLEeKddDatQRNx
                                    TAsZp6moQaeDQBMTTc00NThQA7OKN1IeRTec0ASA0pFMzinK2aBCEVC33qnk4GarbstQMsp92jvQ
                                    n3aB1pgPP3ag/iqZvu1CvLUmBOv3ab/FS9BTR96gLiy/cqiv+tq5O2FqpGMvTAup0pSKQcClzQAm
                                    2lAxRmjNIB1FNooAdRTSaM0ALRSZpCaYDqKaDTqdgCikpRQAtFNJoBpAOoNGaaxoEBNMY4FIzVHI
                                    3y0AOD5NPQc0yzXzWqy8ZQ0XASoJzzU4qtcH5qdwJYuVqSo4fuU9jxQBXl+9ViMfJVdvmarCH5cU
                                    AOFOpMYFJnJoAbK3FMgTzJKWbpVnTo8sDRcDVtIAiA1ZzSKMKKAOaTEOooooAKKKKACiiigAoooo
                                    AKKKKACiiigAooooAKo6pbieAjFXTSHDDBoA82v4TbzEY71GrArXU65pXmZZRXMPZyROeDTAhkGO
                                    ajJzUkh7GosUAKKKBQaAF7UlHagUAJ3oNFKelACCnrTVp60AD0wU9qYKAA0AUpphagBxptKOaGG2
                                    gBNvNTwnaaiU5qROtAGnbvmrqYxWbb8VeVuKQywDikkOUNMU0rn5DQMxbkfva1NOPyis26/1laGn
                                    HigRqE/Iao/8taun7lUW4ekxl+MApT1UVBC/y1Mr8VIFS+4FU4mqe/eqMbVSAuhhTwwqqM04E0wL
                                    O8UeZVfJo5pAW0kBoLjNVlyKXJzQBZLikDjNQ5OKaWINAi4TuWqzcPSpJxTTy1Ay3H92gmkQ4Wgn
                                    mgBHb5ajjPzU9x8tRx/eoAsnpTV60E8UinmgQlx92qsX36sTniq0f3qYy4DxS5qMGlzQwH5pc1Hm
                                    lzSAfmgmmZoJoAcDQTTc0hNMB26lFMFBbbTAlyBRuFVHkJPFIHbFAFssKAwqmWagO1AF0kU3IzVX
                                    e1J5hpAXMimsRVYyGmGQ0CLJxUcuNhqEyGopZTtpgaWkkF6154QRmud0uYrJW49zlKkZWdMGqNyM
                                    NV4yAmql0QTTAWA/LUjn5ajgHFPk6UwIV5arC8CoYxzU/agQueKBSClJwtAEUpycVq6ZH8oNY6fP
                                    KK6OxTbGKQFmg0GigQCloooAKKKKACiiigAooooAKKKKACiiigAooooAKZjmnGikA10Ei4YVnXmm
                                    RtEzADNadNm/1TfSmB5nqKeVclfeq/UVo62mLpj71mr0pgKKDQKDQAdqQUvakFAAaXtSGndqAEWn
                                    imLUgoARulRjrUrdKjNACnpTCOadmkNAEkQ5pbhfl4qJXxSs5YUwI14qaM1Fg+lPTrQBoQtVxG4r
                                    Phq0hwKQy2GpxOVqsG4qRWytIZnXn36t6a1VL371T6c3NIRtk/JVKT71WWb5KqOfmoGWIzxUgaoY
                                    z8tSKaQFS95FVIuKu3nSqKHmqQFgGnA1EKeKYh4NOBpmaUGkBJnFIDzTM5oFICXNMejNB6UAIDin
                                    qeaiNCE7qBl5T8tIDzSJ92gnmgB7H5ahT71PZvlpkf3qAJSeKRTzQ54pimgBJ24qGM/NT5zUcXWm
                                    MsZpc02jNDAdmjNNzS0hDs0ZpuaQmgB5NJnmmg0poAeDxUE74qTNV5zkUwCNtxqcYqpD1qwDTAcc
                                    UnFNZqaGpASmmGjNJmkApqM04mmZ5piBulV5Dzip2PFVW+/TAuWSYOa0STiqlkOBVpmxUjDkCqsh
                                    y1WS3y1XxlqYFiHhaSQ0JwKa5pgOjqUmoUp+aAHimueKXtUZOWxQBPYw7pc10ES7VArP06LGDitO
                                    kIKKKBQIWiiigAooooAKKKKACiiigAooooAKKKKACiiigApBS0lABSSf6tvpS0jcqaAOC19MTE1j
                                    p0rpPEUWCTXNx96aATvTj0pD1pT0pgIOlA60UnegBTQOlB6UCkAnepFqM9akSgBzdKiqY9KiagBD
                                    SYpwFKcCgCPFOXANGRmlwKYEhKleBUe05zU8KKetSSRg8LSAZA4HWrCtzVFo3iOTmrUMqlaALBan
                                    KSBUQNOL8VIyC65osmKtSycio4jh+KYG2p3JULjBpYXO0U2VqBomj+7T1zmoIX4qTzMUANux8tZ6
                                    feq3cygiqi4zQInBAFLvFQs1M30gLG6jdUIalLUwJfMpQ9QA5p/QUwJDJR5magY5oU4pAWAafGRm
                                    oA2aeh5oGXA3FAOTUaning0AK3ShCBTXbiow3NAErkk0qmmFuKQNxQAyc80iDimyNk05DxQIkyaN
                                    1NJpuaBol3UbqjzSZNFhku6gHNRA04HFFgJMEUbqaWOKi3HdQBO3SqkjZNTs3y1TZsvQK5Yj4qTd
                                    UAbApd9AEpNNJpm+mNJQBLvppeoTJTDLQIsb6TfVcy0gkyaBE7NTEIL0A5FIi/vM0AasI2qMVIea
                                    ijPyCn7qYwamDrSls0gHNAEueKjJ5px4FNyKBj1YClByaiHJqUEAUCHM+BT7WPzZBUSr5hwK2dOt
                                    dgyRSEXbeMJGKlo6UnegBaKKBQAtFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABSGlpKAAUdqK
                                    KAOa8Sxfuya41OGNd94gi3W5PtXBONkpHvTQCtR2ofpSjpTAbSUvegigA7UCl7UlIBDT0php8dAD
                                    26U1VLGnNwKmtsMaAIvKIHSonVvStoQqV6U37KpPSgDECP6VPHEW61q/ZVHao5IwnSgDNkUx9Kv6
                                    SvnzANVS4ORWpoMJ80EUAamo6MGt9yjnFc21jNE5+U4r0dVDRAN6VDJYQv8AwikB58RIvY00mT0N
                                    drNo8bHgVAdFXP3aQzj2MhHQ02Fir/NXZ/2GhXpWNqelmDJUUwIon3KMUktVreTY2GqeRg3egaHw
                                    mllbAqKM4709yNtAFOaQ5pImNNmYZoR+KBE55phFJvo30gDpS80m6lDCmAqnb1p+c1E7elWbdNwo
                                    GNApHFWGjxTSnFICqpINWEao3UA0zftoAuhqcGqmstSrLTAsMaYDTDJTd9AE/WlA4qHeaXzOKQDZ
                                    MZpFfFRudxpu00AWBIKN4qAA07Bp3HYm3ik31DzS4NFxWJQ9P3ioMGgZouFifeKMioOaNxxSCw+R
                                    +1QquTmkOSamjwBQFhGXimEGpWYUxmFMCMg01gaeWFIWFAiEg0wg1KzCoGk5oAXaSacEIpEapd4x
                                    QIRTirMY71WHWp0cAUAW0fih5MVXMoFMLlzxQBbjfcasDpVGLIqyJOKBkjNURzmkLUqnNAx68CkL
                                    FjgUhOeBVuxtS7AkUCLOnWxJBIrdRQi4FR28KxoOKmoEFLSUZoAWikozQAtFFFABRRRQAUUUUAFF
                                    FFABRRRQAUUUUAFFFFABRRSGgBKWiloAz9XTfbke1ee3qGO4OfWvS7tN8R+lefa2my4P1oQFFjkC
                                    lHSmjpQvWqAXHNBpTxSHkUAA5pOhoTrSP1pAKadHTe1LH1oAkk5FSWvDUw9KWI4egDXjPy1KMVXj
                                    b5BUqGgAlbAqjJIWNXpsFaoOMGgCrPXQ+HI92DXPzDcRXW+GosRg0DOgUfKBS0tJSELSUZooAQHm
                                    q17arPEeOatUtAHB3+mSxykoDiqfkTgYINegy28b9VFVzpsRP3RQO5w6wzA9DUnlSsMYNdkdLj9B
                                    QNLjB6CgLnE/2fNIeAatQ6PNjkGuziso0/hFWBGg6KKAucS2jy+hqM6RP6Gu62L/AHRR5af3RQFz
                                    g/7Jn/umo5NMnTsa7/y0/uimvBG4wVFAXPOWheNvmFaFoflrW1rTwoLKKwY5PKODSbGi+5AqCRhj
                                    iojIZOlPjjZqQFWR+ahck1otZlu1QyWhUdKAKYJp4cilMZQ8imZyadwJlel80CtHT9O+0LV0+Hs9
                                    qYjE85cU3eX4Fbn/AAj5qxb6EFPIpDOeS3lY5walNvIB0rrotNjRcYFD6cjdhQFzjvKfPSlMbeld
                                    YdLQ9hTTpKegoKTRymxvSnBD6V1B0hfQUf2QvpSC6OXKN6U3y3z0rqxpC+lL/ZKegoC6OW8tsdKb
                                    5Lk9DXVjSk9BThpaDsKYrnKC2f0pGt5ewNdeNOQdqd/Z8foKAucalpM3Y046fMexrslso1/hFSC3
                                    jx90UybnCvbPF94VAzAV0utQBFJArk5PvmgBWcZxVqHT2mXcKz3Ugg1o2ureRHtxQgI5rfyPvVW3
                                    jPFSXd4bluKhVMDNMCUOBTGmwahIYtxUgt3ftSAQ3BY4FaNmu4ZNU0smHJFTrMYuKQWL8m1RUO/m
                                    oDIXpyZNAyyh3GpwoFVoQQ1XokLsKBXH29uXYVvWdsI1BxUNjb4AJFaQGBTEFFGKWgBDQKWigBKQ
                                    U6igAooooAKKKKACiiigAooooAKKKKACiiigAooooAQ0UtIaAFptKKDQAMMqRXC+JINsxOO9d0K5
                                    3xHa742bFAHFIc8U4cGmINspFSMKYA3SkTkU7GVpq8UwEHWlI5o70tADe1C8GlpvQ0gLHUUi8NQh
                                    yKOhoAuRycVKklVIzxUoOKAJ5JeKrM2aV2zTQODQBGg3Sge9dvoUeyAfSuMs03XAHvXd6YmyAfSg
                                    ZdNFLSUhBS0lJmgBcUUA0YoAWkopaACikooAKKWigBKKWkJxQAhFKKOtGKAM/V8eQc+lcVcDdKce
                                    tdlrf+p/CuYht98vPrSZSJLO1LAZFaCWyp1p6KsEeaoXOpBSQDRYC9hBTWiRqxG1M561atdRDEZN
                                    AiS6tRjIFZUsRjaukV0lSszUIQBkUDNjw7gxit+ud8PttXFdEORQIKWkopiA0c0UZoAWim5paACi
                                    iigAozRRQAZozRiigApaSigBaQUUhoAoarB5sJ4rg75DDMeO9elOodCDXJa9pvJYCgDndwZaRIgx
                                    pgBR9pq7CnGaTKREYgtSQxGQ4xUwjLtitWytAoBIoGQW+mAjJFWlsFXtVovs4FMNwB1pAQvbKBjF
                                    Z9zZ9wK1BOjnrTmQOKAMFI9pwalEdW7iDacimxLnrTJCKPpWvYW+SCRVW0gLyAYrftoBGo4piJo0
                                    CKKfSGigAzS00jmnUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFJS0UAJRS0
                                    lAC1S1SIS2rcVdqOdPMjK+tAHmd5F5Vw31qL7wrW8QWhhkLYrIgO4YNMBwOOKRhihhh6V6YDTwKB
                                    0o6ik6UAKKa/FOFIw3UASRHIp7VFG204qZuRSAENPLVGnWnGgA3Emp0Hy02OPPNSM4HFAyfSot90
                                    PrXc26bIgPauT0ODMwauwHCigAooBopCCkIpaKAAcUZpaTFABRS0lABiilooASkJp1JigAFBFLSU
                                    AA4ooooAzNZ/1VY1qMNmtnWf9XWVbgbTQNEGpXOyMgGuYlkaSQ81t6qCc1igYbpQA0KTSB2jfg08
                                    nBpMbj0oYI3tLnLgAmr13HvWszS0K4rWlPy0hk+kJsNdAvSsHTm+at1fuimIWjFFFAgoxRRQAYoo
                                    ooAKKKMUAFFFFABRRRQAtFJRQAUlFFAAOtV723E0JGOas0dRQB57qlkYZycUlv8AcrqdXsRMCQK5
                                    1o/KOzFAya1QFq1R8qcVm2qkGtEn93QBQu7ny881i3GosWwpqbVZDuIFZsQBPNPQC1DfurDJrdsb
                                    wSKOa5tkBPFaGmBg4oFqb8qblzVZE+fAq6D+7pbaDfJmoZSL+n24UBiK0ajhTYgFSU0JhRS0lMQt
                                    JRS0AFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUlAGJr9l58
                                    ZbFcLKv2eYrXqM8YljKmuH17TGjkLgUwMlhuXIpsZzwaZHJj5DTsY5FAC7eaRutPDcVGx5pgO7Ug
                                    pvNKDigBOjZqypBWqzHNOiJBwaTAeMhqvRIrLmqjDjIp8UpBxQBZJC8VE/MgxSuflzUllH50wxQM
                                    6bQ4CIwcVu9qqadD5UAGKtmkIMUtIaKAFopKWgAooooAKKKKACiiigAooooASiiigApaSloAzNYG
                                    Y6x4DjIroL6LzUrBliaJjxQNFe8g8wVmNZ4PStcszcYqNomPagdjINmSelTw2QB5FXRGQelSiNj2
                                    oCwttEsYqWUg8UgjYCnJAzN0oEWtOj+YGt1fuis+yg2gcVoCgQtFFFABRRRQAUUUUAFFFFABRRRQ
                                    AUUUUAFFFFACGilooASgUUdKAGSIGQiuY1S0KyFgK6rNU7+2EiE4oBHN2oyeav7fkxVdkELnNSJc
                                    J3NBRk6laFsnFZaW7BsYrrH8qUc4qubWIHPFAjFis2PatOytNhBxVyNIl9Kf5iL0oAH+Vas2DfNV
                                    B5N54rR09eRQBrp0p1IvSloEFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFF
                                    ABRRRQAUUUUAFFFFABSN0paSgBFqlqdotxCeOcVeoIBGDQB5nqWmyQSkhTiqPmMvBFemXunR3CHg
                                    ZrnLjw6TISFpgctvb0oDnNdP/wAI6237tV38OydlNAGGH4oyDWu/h+YdjUDaJOp6GgDPAGadkZq2
                                    2kTgdDUZ0ucdjTARHBGM0/5V5qMafOD0NSCymPGDSAbuMh2iuh8P2B3BmFVNL0h2kBYV2FpbrBGA
                                    BSGTqAoAFLRS0CG06kNANAC0UUUAFFFFABRRRQAUUUUAFFFFACUUUUAFFFFAAQD1qpPaLIelWzRQ
                                    BnDTgO1O+wL6VfooHczjp6ntT0sFHar2aM0BcqGzX0pUtVB6VbooENVQo4paKKAFooooAKKKKACi
                                    iigAooooAKKKKACiiigAooooAQ0lKTiigABpaSg0AFI67hilFHegDHvrAvkiss6fLu4zXWEButMM
                                    S+lAHNLYTD1pwsJT610hjXHSkEYHagDAXTpfeg6bJXQhR6Uu0elAGDFprBua1be28oCrOAKDQAUt
                                    NFOoAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACi
                                    iigApKWigApMD0paKAEwPSjaPQUtFADSinqophgjP8IqWigCA2sR/hFMNjEf4RVqigCmdOiPYU0a
                                    bED0FXqKAIooEiHAFSUtFACUUtFACUAUtFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAlLR
                                    RQAlFLRQAmKKWigAooooAKKKKACiiigAooooAKKKKACiiigAooooAKKKKACiiigBCM0tFFABSUtF
                                    ACYpaKKAExQaWigBBS0UUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAB
                                    RRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFF
                                    FFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUU
                                    UAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQ
                                    AUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAB
                                    RRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFF
                                    FFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUU
                                    UAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQ
                                    AUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAB
                                    RRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFF
                                    FFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUU
                                    UAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQ
                                    AUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFAB
                                    RRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFF
                                    FFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUU
                                    UAFFFFABRRRQAlFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUU
                                    AFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQA
                                    UUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABR
                                    RRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQAUUUUAFFFFABRRRQB/9k='
);

create table goods_category(
    id          serial,
    name        varchar(20) unique);

insert into goods_category(name) values ('Фильтра');
insert into goods_category(name) values ('Компрессора');
insert into goods_category(name) values ('Шланги');
insert into goods_category(name) values ('Масла');
insert into goods_category(name) values ('Прокладки');
insert into goods_category(name) values ('Разное');


create table goods (
    id              serial primary key,
    price           numeric         not null,
    qnt             int             not null,
    category        varchar(20)     references goods_category(name) not null,
    title           varchar(50)     not null,
    description     varchar(255)    not null,
    producedby      varchar(255)    default null,
    trademark       varchar(50)     default null,
    cars            varchar(255)    default null,
    codeid          varchar(50)     default null,
    codes           varchar(255)    default null,
    state           varchar(20)     default null,
    pic             int references small_pics(id) default null
);


create view goodsview as
    select goods.*, small_pics.base64
    from goods left join small_pics
            on goods.pic=small_pics.id;

--- id, price , qnt,
--- category, title,
--- description ,
--- producedby, trademark , cars,
--- codeid, codes,
--- state, pic

insert into goods(price , qnt , category , title , description , producedby , trademark , cars , codeid , codes , state , pic ) values (12.4, 4,
    'Компрессора', 'Компрессор 88320-42110 R',
    'Компрессор восст. Toyota RAV 4 II 2.0D 01-',
    'TOYOTA', 'MSG Rebuilding', 'TOYOTA, LEXUS, ACURA',
    '8831042210, 8832042110', '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437',
    'brand new', null
);
insert into goods(price , qnt , category , title , description , producedby , trademark , cars , codeid , codes , state , pic ) values (12.4, 4,
    'Компрессора', 'Компрессор 6754-88',
    'Toyota Corolla',
    'TOYOTA', 'MSG Rebuilding', 'TOYOTA, LEXUS, ACURA',
    '88310672210, 9874646', '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437',
    'brand new', 1
);
insert into goods(price , qnt , category , title , description , producedby , trademark , cars , codeid , codes , state , pic ) values (12.4, 4,
    'Прокладки', 'Компрессор 88320-42110 R',
    'Компрессор восст. Toyota RAV 4 II 2.0D 01-',
    'TOYOTA', 'MSG Rebuilding', 'TOYOTA, LEXUS, ACURA',
    '8831042210, 8832042110', '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437',
    'brand new', 1
);

insert into goods(price , qnt , category , title , description , producedby , trademark , cars , codeid , codes , state , pic ) values (12.4, 4,
    'Фильтра', 'Компрессор 88320-42110 R',
    'Компрессор восст. Toyota RAV 4 II 2.0D 01-',
    'TOYOTA', 'MSG Rebuilding', 'TOYOTA, LEXUS, ACURA',
    '8831042210, 8832042110', '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437',
    'brand new', 1
);

insert into goods(price , qnt , category , title , description , producedby , trademark , cars , codeid , codes , state , pic ) values (12.4, 4,
    'Фильтра', 'Компрессор 88320-42110 R',
    'Компрессор восст. Toyota RAV 4 II 2.0D 01-',
    'TOYOTA', 'MSG Rebuilding', 'TOYOTA, LEXUS, ACURA',
    '8831042210, 8832042110', '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437',
    'brand new', 1
);

insert into goods(price , qnt , category , title , description , producedby , trademark , cars , codeid , codes , state , pic ) values (12.4, 4,
    'Шланги', 'Компрессор 88320-42110 R',
    'Компрессор восст. Toyota RAV 4 II 2.0D 01-',
    'TOYOTA', 'MSG Rebuilding', 'TOYOTA, LEXUS, ACURA',
    '8831042210, 8832042110', '15236, 240943, 32634G, 510215, 700510215, 8FK351114401, 92030169, ACP314, DCP50032, K15236, T55830, TOK437',
    'brand new', 1
);

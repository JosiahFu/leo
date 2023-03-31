import './SelectFromList.scss';
import {ChangeEvent, ReactElement, useEffect, useState} from 'react';

export enum Display {
  DROP_DOWN,
  SCROLLING_LIST,
}

export function SelectFromList<K, V>(props: {
  display: Display;
  values: Array<V> | Map<K, V>;
  selectedKey: K;
  getKey: (value?: V) => K;
  stringToKey: (key: string) => K;
  compareValues: (a: V, b: V) => number;
  renderValue: (key: K) => ReactElement;
  onSelect: (key: K) => void;
}): ReactElement {
  const emptyKey = props.getKey();
  const [sortedValues, setSortedValues] = useState(new Array<V>());

  useEffect(() => {
    const sortedValues = Array.from(props.values.values());
    sortedValues.sort(props.compareValues);
    setSortedValues(sortedValues);
  }, [props.values]);

  useEffect(() => {
    props.onSelect(props.selectedKey);
  }, [props.selectedKey]);

  switch (props.display) {
    case Display.DROP_DOWN:
      return (
        <select
          value={String(props.selectedKey)}
          onChange={(e: ChangeEvent<HTMLSelectElement>) => {
            props.onSelect(props.stringToKey(e.target.value));
          }}
        >
          <option key={String(emptyKey)} value={String(emptyKey)}>
            {props.renderValue(emptyKey)}
          </option>
          {sortedValues.map(value => {
            const key = props.getKey(value);
            return (
              <option key={String(key)} value={String(key)}>
                {props.renderValue(key)}
              </option>
            );
          })}
        </select>
      );
    case Display.SCROLLING_LIST:
      return (
        <div style={{width: '100%'}}>
          <div
            key={String(emptyKey)}
            onSelect={() => props.onSelect(emptyKey)}
            className={
              props.selectedKey === emptyKey ? 'selected' : 'not-selected'
            }
          >
            {props.renderValue(emptyKey)}
          </div>
          {sortedValues.map(value => {
            const key = props.getKey(value);
            return (
              <div
                key={String(key)}
                onSelect={() => props.onSelect(key)}
                className={
                  props.selectedKey === key ? 'selected' : 'not-selected'
                }
              >
                {props.renderValue(key)}
              </div>
            );
          })}
        </div>
      );
    default:
      return <>Display not recognized {Display[props.display]}</>;
  }
}
